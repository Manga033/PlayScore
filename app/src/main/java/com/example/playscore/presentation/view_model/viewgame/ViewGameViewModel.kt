package com.example.playscore.presentation.view_model.viewgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.datasource.network.dto.UpdateGameDto
import com.example.playscore.model.repository.game.GameRepository
import com.example.playscore.model.repository.network.GameNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
    private val gameNetworkRepository: GameNetworkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewGameUiState>(ViewGameUiState.Init)
    val uiState: StateFlow<ViewGameUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<ViewGameNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val gameId: Int = savedStateHandle["gameId"] ?: 0
    private var syncMessage: String = ""

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            _uiState.value = ViewGameUiState.Loading
            try {
                combine(
                    gameRepository.observeGameById(gameId),
                    gameRepository.observeScoreHistoryForGame(gameId)
                ) { game, scoreLog -> game to scoreLog }
                    .collect { (game, scoreLog) ->
                        _uiState.value = if (game == null) {
                            ViewGameUiState.Error("Game not found")
                        } else {
                            ViewGameUiState.Success(game, scoreLog, syncMessage)
                        }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = ViewGameUiState.Error(e.message ?: "Failed to load game.")
            }
        }
    }

    fun increaseScore(playerId: Int, currentScore: Int) {
        updateScore(playerId, currentScore + 1)
    }

    fun decreaseScore(playerId: Int, currentScore: Int) {
        if (currentScore > 0) {
            updateScore(playerId, currentScore - 1)
        }
    }

    fun deleteGame() {
        viewModelScope.launch {
            try {
                val messages = mutableListOf<String>()
                gameRepository.deleteGameById(gameId)
                try {
                    gameNetworkRepository.deleteGame(gameId)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    messages.add("Network delete failed.")
                }
                syncMessage = if (messages.isEmpty()) {
                    "Game deleted."
                } else {
                    messages.joinToString(" ")
                }
                _navigationEvent.send(ViewGameNavigationEvent.NavigateBack(syncMessage))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ViewGameUiState.Error(e.message ?: "Failed to delete game.")
            }
        }
    }

    private fun updateScore(playerId: Int, score: Int) {
        viewModelScope.launch {
            try {
                gameRepository.updatePlayerScore(playerId, score)
                val currentState = _uiState.value as? ViewGameUiState.Success
                val game = currentState?.game
                if (game != null) {
                    try {
                        gameNetworkRepository.updateGame(
                            gameId,
                            UpdateGameDto(
                                name = game.name,
                                type = game.type,
                                date = game.date
                            )
                        )
                        syncMessage = "Score updated."
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        syncMessage = "Network update failed."
                    }
                } else {
                    syncMessage = "Score updated."
                }
                (_uiState.value as? ViewGameUiState.Success)?.let { state ->
                    _uiState.value = state.copy(syncMessage = syncMessage)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ViewGameUiState.Error(e.message ?: "Failed to update score.")
            }
        }
    }
}


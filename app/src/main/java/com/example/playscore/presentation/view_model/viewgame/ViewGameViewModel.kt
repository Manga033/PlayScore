package com.example.playscore.presentation.view_model.viewgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.repository.game.GameRepository
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
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewGameUiState>(ViewGameUiState.Init)
    val uiState: StateFlow<ViewGameUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<ViewGameNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val gameId: Int = savedStateHandle["gameId"] ?: 0

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
                            ViewGameUiState.Success(game, scoreLog)
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
                gameRepository.deleteGameById(gameId)
                _navigationEvent.send(ViewGameNavigationEvent.NavigateBack)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = ViewGameUiState.Error(e.message ?: "Failed to delete game.")
            }
        }
    }

    private fun updateScore(playerId: Int, score: Int) {
        viewModelScope.launch {
            try {
                gameRepository.updatePlayerScore(playerId, score)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = ViewGameUiState.Error(e.message ?: "Failed to update score.")
            }
        }
    }
}


package com.example.playscore.presentation.view_model.creategame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.domain.Player
import com.example.playscore.model.repository.game.GameRepository
import com.example.playscore.presentation.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateGameUiState>(CreateGameUiState.Init)
    val uiState: StateFlow<CreateGameUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<CreateGameNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun createGame(
        name: String,
        type: String,
        playerNames: List<String>
    ) {
        viewModelScope.launch {
            _uiState.value = CreateGameUiState.Loading
            try {
                if (!Validation.hasRequiredPlayersForGameType(type, playerNames)) {
                    _uiState.value = CreateGameUiState.Error(
                        if (type == "Sports") {
                            "At least two teams are required."
                        } else {
                            "At least one player is required."
                        }
                    )
                    return@launch
                }
                val players = playerNames
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { name -> Player(name = name, score = 0) }
                gameRepository.createGame(name.trim(), type, players)
                _uiState.value = CreateGameUiState.Success
                _navigationEvent.send(CreateGameNavigationEvent.NavigateBack)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = CreateGameUiState.Error(e.message ?: "Failed to create game.")
            }
        }
    }
}

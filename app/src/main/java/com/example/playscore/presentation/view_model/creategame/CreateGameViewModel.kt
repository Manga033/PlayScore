package com.example.playscore.presentation.view_model.creategame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.datasource.network.dto.CreateGameDto
import com.example.playscore.model.domain.Player
import com.example.playscore.model.repository.auth.AuthRepository
import com.example.playscore.model.repository.cloud.CloudGameRepository
import com.example.playscore.model.repository.game.GameRepository
import com.example.playscore.model.repository.network.GameNetworkRepository
import com.example.playscore.presentation.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameNetworkRepository: GameNetworkRepository,
    private val cloudGameRepository: Provider<CloudGameRepository>,
    private val authRepository: Provider<AuthRepository>
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
                val gameName = name.trim()
                val date = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
                val localGameId = gameRepository.createGame(gameName, type, players)
                val syncMessage = syncGame(localGameId, gameName, type, players, date)
                _uiState.value = CreateGameUiState.Success(syncMessage)
                _navigationEvent.send(CreateGameNavigationEvent.NavigateBack(syncMessage))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CreateGameUiState.Error(e.message ?: "Failed to create game.")
            }
        }
    }

    private suspend fun syncGame(
        localGameId: Int,
        name: String,
        type: String,
        players: List<Player>,
        date: String
    ): String {
        val messages = mutableListOf<String>()

        try {
            gameNetworkRepository.createGame(
                CreateGameDto(
                    name = name,
                    type = type,
                    date = date
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            messages.add("Network sync failed.")
        }

        if (authRepository.get().isLoggedIn()) {
            try {
                cloudGameRepository.get().addGame(
                    localId = localGameId,
                    name = name,
                    type = type,
                    players = players,
                    date = date
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                messages.add("Cloud sync failed.")
            }
        } else {
            messages.add("Login to sync this game to cloud.")
        }

        return if (messages.isEmpty()) {
            "Game saved and synced."
        } else {
            messages.joinToString(" ")
        }
    }
}

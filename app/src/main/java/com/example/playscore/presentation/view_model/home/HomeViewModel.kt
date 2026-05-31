package com.example.playscore.presentation.view_model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.datasource.network.mapper.toGame
import com.example.playscore.model.domain.Game
import com.example.playscore.model.repository.auth.AuthRepository
import com.example.playscore.model.repository.cloud.CloudGameRepository
import com.example.playscore.model.repository.game.GameRepository
import com.example.playscore.model.repository.network.GameNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameNetworkRepository: GameNetworkRepository,
    private val cloudGameRepository: Provider<CloudGameRepository>,
    private val authRepository: Provider<AuthRepository>
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Init)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val selectedType = MutableStateFlow("All")
    private val networkGames = MutableStateFlow<List<Game>>(emptyList())
    private val cloudGames = MutableStateFlow<List<Game>>(emptyList())
    private val networkMessage = MutableStateFlow("")
    private val cloudMessage = MutableStateFlow("")
    private val syncMessage = MutableStateFlow("")
    private val isLoggedIn = MutableStateFlow(false)
    private var cloudJob: Job? = null

    private data class SyncState(
        val networkGames: List<Game>,
        val cloudGames: List<Game>,
        val networkMessage: String,
        val cloudMessage: String,
        val syncMessage: String,
        val isLoggedIn: Boolean
    )

    private data class MessageState(
        val networkMessage: String,
        val cloudMessage: String,
        val syncMessage: String,
        val isLoggedIn: Boolean
    )

    init {
        refreshLoginSession()
        loadGames()
        loadNetworkGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                gameRepository.seedGamesIfNeeded()
                val messageState = combine(
                    networkMessage,
                    cloudMessage,
                    syncMessage,
                    isLoggedIn
                ) { networkMessage, cloudMessage, syncMessage, isLoggedIn ->
                    MessageState(
                        networkMessage = networkMessage,
                        cloudMessage = cloudMessage,
                        syncMessage = syncMessage,
                        isLoggedIn = isLoggedIn
                    )
                }

                val syncState = combine(
                    networkGames,
                    cloudGames,
                    messageState
                ) { networkGames, cloudGames, messageState ->
                    SyncState(
                        networkGames = networkGames,
                        cloudGames = cloudGames,
                        networkMessage = messageState.networkMessage,
                        cloudMessage = messageState.cloudMessage,
                        syncMessage = messageState.syncMessage,
                        isLoggedIn = messageState.isLoggedIn
                    )
                }

                combine(
                    gameRepository.observeGames(),
                    gameRepository.observeGameTypes(),
                    selectedType,
                    syncState
                ) { games, gameTypes, selectedType, sync ->
                    HomeUiState.Success(
                        games = games,
                        networkGames = sync.networkGames,
                        cloudGames = sync.cloudGames,
                        networkMessage = sync.networkMessage,
                        cloudMessage = sync.cloudMessage,
                        syncMessage = sync.syncMessage,
                        isLoggedIn = sync.isLoggedIn,
                        gameTypes = gameTypes,
                        selectedType = selectedType
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load games.")
            }
        }
    }

    fun refreshLoginSession() {
        try {
            isLoggedIn.value = authRepository.get().isLoggedIn()
            observeCloudGames()
        } catch (e: Exception) {
            isLoggedIn.value = false
            cloudGames.value = emptyList()
        }
    }

    private fun loadNetworkGames() {
        viewModelScope.launch {
            try {
                networkGames.value = gameNetworkRepository.getGames().map { it.toGame() }
                networkMessage.value = "Network games loaded."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                networkMessage.value = "Network sync is not available."
            }
        }
    }

    private fun observeCloudGames() {
        cloudJob?.cancel()
        cloudJob = viewModelScope.launch {
            try {
                cloudGameRepository.get().observeCloudGames().collect { games ->
                    cloudGames.value = games
                    cloudMessage.value = "Cloud games synced."
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                cloudMessage.value = "Cloud sync is not available."
            }
        }
    }

    fun selectGameType(type: String) {
        selectedType.value = type
    }

    fun showSyncMessage(message: String) {
        syncMessage.value = message
        loadNetworkGames()
    }

    fun logout() {
        try {
            authRepository.get().logout()
        } catch (e: Exception) {
        }
        isLoggedIn.value = false
        cloudGames.value = emptyList()
        observeCloudGames()
    }
}


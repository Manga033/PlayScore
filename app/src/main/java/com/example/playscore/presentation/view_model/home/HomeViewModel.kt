package com.example.playscore.presentation.view_model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.repository.game.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Init)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val selectedType = MutableStateFlow("All")

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                gameRepository.seedGamesIfNeeded()
                combine(
                    gameRepository.observeGames(),
                    gameRepository.observeGameTypes(),
                    selectedType
                ) { games, gameTypes, selectedType ->
                    HomeUiState.Success(
                        games = games,
                        gameTypes = gameTypes,
                        selectedType = selectedType
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load games.")
            }
        }
    }

    fun selectGameType(type: String) {
        selectedType.value = type
    }
}


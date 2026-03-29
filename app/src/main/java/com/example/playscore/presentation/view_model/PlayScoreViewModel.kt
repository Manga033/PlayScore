package com.example.playscore.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.playscore.data.repository.GameRepository
import com.example.playscore.presentation.ui.screen.home.util.HomeUiState
import com.example.playscore.presentation.ui.screen.viewgame.util.ViewGameUiState

class PlayScoreViewModel : ViewModel() {
    private val _homeUiState = mutableStateOf(
        HomeUiState(games = GameRepository.getGames())
    )
    val homeUiState: State<HomeUiState> = _homeUiState

    private val _viewGameUiState = mutableStateOf(
        ViewGameUiState()
    )
    val viewGameUiState: State<ViewGameUiState> = _viewGameUiState

    fun selectGame(gameId: Int) {
        val game = _homeUiState.value.games.find {
            it.id == gameId
        }
        _viewGameUiState.value = ViewGameUiState(game = game)
    }
}
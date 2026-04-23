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

    private val _viewGameUiState = mutableStateOf(ViewGameUiState())
    val viewGameUiState: State<ViewGameUiState> = _viewGameUiState

    val totalGamesCount: Int
        get() = _homeUiState.value.totalGames

    val hasGames: Boolean
        get() = _homeUiState.value.hasGames

    val boardGamesCount: Int
        get() = _homeUiState.value.boardGames.size

    val sportsGamesCount: Int
        get() = _homeUiState.value.sportsGames.size

    fun selectGame(gameId: Int) {
        val game = GameRepository.getGameById(gameId)
        _viewGameUiState.value = ViewGameUiState(game = game)
    }

    fun selectGameType(type: String) {
        _homeUiState.value = _homeUiState.value.copy(selectedType = type)
    }

    fun getGameTypes(): List<String> {
        return GameRepository.getGameTypes()
    }
}
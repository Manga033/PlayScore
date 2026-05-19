package com.example.playscore.presentation.view_model.viewgame

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.ScoreLogEntry

sealed interface ViewGameUiState {
    data object Init : ViewGameUiState
    data object Loading : ViewGameUiState
    data class Success(
        val game: Game,
        val scoreLog: List<ScoreLogEntry>
    ) : ViewGameUiState {
        val playerCount: Int
            get() = game.players.size

        val totalScore: Int
            get() = game.players.sumOf { it.score }

        val winnerName: String
            get() = game.players.maxByOrNull { it.score }?.name ?: "N/A"
    }
    data class Error(val message: String) : ViewGameUiState
}


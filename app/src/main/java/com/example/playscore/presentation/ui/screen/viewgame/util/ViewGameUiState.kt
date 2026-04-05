package com.example.playscore.presentation.ui.screen.viewgame.util

import com.example.playscore.data.repository.Game

data class ViewGameUiState(
    val game: Game? = null
) {
    val hasGame: Boolean
        get() = game != null

    val playerCount: Int
        get() = game?.players?.size ?: 0

    val winnerName: String
        get() = game?.players?.maxByOrNull { it.score }?.name ?: "N/A"
}
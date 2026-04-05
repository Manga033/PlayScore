package com.example.playscore.presentation.ui.screen.home.util

import com.example.playscore.data.repository.Game

data class HomeUiState(
    val games: List<Game> = emptyList()
) {
    val totalGames: Int
        get() = games.size

    val hasGames: Boolean
        get() = games.isNotEmpty()

    val boardGames: List<Game>
        get() = games.filter { it.type == "Board Game" }

    val sportsGames: List<Game>
        get() = games.filter { it.type != "Board Game" }
}
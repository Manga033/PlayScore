package com.example.playscore.presentation.ui.screen.home.util

import com.example.playscore.data.repository.Game

data class HomeUiState(
    val games: List<Game> = emptyList(),
    val selectedType: String = "All"
) {
    val totalGames: Int
        get() = games.size

    val hasGames: Boolean
        get() = games.isNotEmpty()

    val boardGames: List<Game>
        get() = games.filter { it.type == "Board Game" }

    val sportsGames: List<Game>
        get() = games.filter { it.type != "Board Game" }

    val recentGames: List<Game>
        get() = games.sortedByDescending { it.id }.take(3)

    val filteredByType: List<Game>
        get() = if (selectedType == "All") games
        else games.filter { it.type == selectedType }

    fun countForType(type: String): Int =
        if (type == "All") games.size
        else games.count { it.type == type }
}
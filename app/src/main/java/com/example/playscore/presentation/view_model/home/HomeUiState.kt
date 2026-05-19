package com.example.playscore.presentation.view_model.home

import com.example.playscore.model.domain.Game

sealed interface HomeUiState {
    data object Init : HomeUiState
    data object Loading : HomeUiState
    data class Success(
        val games: List<Game> = emptyList(),
        val gameTypes: List<String> = listOf("All"),
        val selectedType: String = "All"
    ) : HomeUiState {
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
            get() = if (selectedType == "All") games else games.filter { it.type == selectedType }

        fun countForType(type: String): Int {
            return if (type == "All") games.size else games.count { it.type == type }
        }
    }
    data class Error(val message: String) : HomeUiState
}


package com.example.playscore.presentation.ui.screen.home.util

import com.example.playscore.data.repository.Game

data class HomeUiState(
    val games: List<Game> = emptyList()
)
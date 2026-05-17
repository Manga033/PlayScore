package com.example.playscore.presentation.view_model.creategame

sealed interface CreateGameUiState {
    data object Init : CreateGameUiState
    data object Loading : CreateGameUiState
    data object Success : CreateGameUiState
    data class Error(val message: String) : CreateGameUiState
}


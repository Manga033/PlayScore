package com.example.playscore.presentation.view_model.register

sealed interface RegisterUiState {
    data object Init : RegisterUiState
    data object Loading : RegisterUiState
    data class Success(val isRegistered: Boolean) : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}


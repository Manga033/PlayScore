package com.example.playscore.presentation.view_model.creategame

sealed interface CreateGameNavigationEvent {
    data class NavigateBack(val message: String) : CreateGameNavigationEvent
}


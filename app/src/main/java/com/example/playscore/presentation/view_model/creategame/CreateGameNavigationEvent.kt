package com.example.playscore.presentation.view_model.creategame

sealed interface CreateGameNavigationEvent {
    data object NavigateBack : CreateGameNavigationEvent
}


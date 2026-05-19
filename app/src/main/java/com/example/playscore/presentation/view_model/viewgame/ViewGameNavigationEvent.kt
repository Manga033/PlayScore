package com.example.playscore.presentation.view_model.viewgame

sealed interface ViewGameNavigationEvent {
    data object NavigateBack : ViewGameNavigationEvent
}

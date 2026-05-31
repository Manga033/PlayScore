package com.example.playscore.presentation.view_model.viewgame

sealed interface ViewGameNavigationEvent {
    data class NavigateBack(val message: String) : ViewGameNavigationEvent
}

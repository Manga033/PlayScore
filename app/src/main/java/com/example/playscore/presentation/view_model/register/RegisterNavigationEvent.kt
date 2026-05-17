package com.example.playscore.presentation.view_model.register

sealed interface RegisterNavigationEvent {
    data object Navigate : RegisterNavigationEvent
}


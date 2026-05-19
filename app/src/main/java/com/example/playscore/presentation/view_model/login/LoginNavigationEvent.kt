package com.example.playscore.presentation.view_model.login

sealed interface LoginNavigationEvent {
    data object Navigate : LoginNavigationEvent
}


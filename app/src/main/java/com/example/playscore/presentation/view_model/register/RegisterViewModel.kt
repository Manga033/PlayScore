package com.example.playscore.presentation.view_model.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Init)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<RegisterNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val isRegistered = userRepository.register(
                    username = username.trim(),
                    email = email.trim(),
                    password = password
                )
                if (isRegistered) {
                    _uiState.value = RegisterUiState.Success(isRegistered = true)
                    _navigationEvent.send(RegisterNavigationEvent.Navigate)
                } else {
                    _uiState.value = RegisterUiState.Error("A user with this email already exists.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Registration failed.")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = RegisterUiState.Init
    }
}


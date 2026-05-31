package com.example.playscore.presentation.view_model.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playscore.model.repository.auth.AuthRepository
import com.example.playscore.model.repository.user.UserRepository
import com.example.playscore.presentation.util.GoogleAuthClient
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
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val googleAuthClient: GoogleAuthClient
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<LoginNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            if (authRepository.isLoggedIn()) {
                _uiState.value = LoginUiState.Success(isLoggedIn = true)
                _navigationEvent.send(LoginNavigationEvent.Navigate)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                authRepository.login(email.trim(), password)
                val localUser = userRepository.login(email.trim(), password)
                if (localUser == null) {
                    userRepository.register(
                        username = email.substringBefore("@"),
                        email = email.trim(),
                        password = password
                    )
                }
                _uiState.value = LoginUiState.Success(isLoggedIn = true)
                _navigationEvent.send(LoginNavigationEvent.Navigate)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Login failed.")
            }
        }
    }

    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val idToken = googleAuthClient.getGoogleIdToken(context)
                val firebaseUser = authRepository.signInWithGoogle(idToken)

                if (firebaseUser == null || firebaseUser.email.isNullOrBlank()) {
                    _uiState.value = LoginUiState.Error("Google sign-in failed.")
                    return@launch
                }

                val email = firebaseUser.email.orEmpty()
                val username = firebaseUser.displayName ?: "Google User"
                val localUser = userRepository.login(email, "GOOGLE_SIGN_IN")

                if (localUser == null) {
                    userRepository.register(
                        username = username,
                        email = email,
                        password = "GOOGLE_SIGN_IN"
                    )
                }

                _uiState.value = LoginUiState.Success(isLoggedIn = true)
                _navigationEvent.send(LoginNavigationEvent.Navigate)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Google login failed.")
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = LoginUiState.Init
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Init
    }
}


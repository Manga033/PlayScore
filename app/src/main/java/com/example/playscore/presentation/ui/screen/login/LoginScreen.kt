package com.example.playscore.presentation.ui.screen.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.component.ScreenHeader
import com.example.playscore.presentation.ui.screen.login.component.LoginFormField
import com.example.playscore.presentation.util.Validation
import com.example.playscore.presentation.view_model.login.LoginNavigationEvent
import com.example.playscore.presentation.view_model.login.LoginUiState
import com.example.playscore.presentation.view_model.login.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                LoginNavigationEvent.Navigate -> onLoginSuccess()
            }
        }
    }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val emailError by remember {
        derivedStateOf {
            if (email.isNotBlank() && !Validation.isEmailValid(email)) {
                "Enter a valid email address"
            } else ""
        }
    }

    val passwordError by remember {
        derivedStateOf {
            if (password.isNotBlank() && !Validation.isPasswordValid(password)) {
                "Password must be at least 6 characters long"
            } else ""
        }
    }

    val isFormValid by remember {
        derivedStateOf {
            Validation.isEmailValid(email) && Validation.isPasswordValid(password)
        }
    }

    LoginScreen(
        email = email,
        onEmailChange = { email = it },
        emailError = emailError,
        password = password,
        onPasswordChange = { password = it },
        passwordError = passwordError,
        passwordVisible = passwordVisible,
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        isFormValid = isFormValid && uiState !is LoginUiState.Loading,
        errorMessage = (uiState as? LoginUiState.Error)?.message.orEmpty(),
        onNavigateToRegister = onNavigateToRegister,
        onNavigateBack = onNavigateBack,
        onLoginClick = {
            viewModel.login(email, password)
        }
    )
}

@Composable
private fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    isFormValid: Boolean,
    errorMessage: String,
    onNavigateToRegister: () -> Unit,
    onNavigateBack: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Login",
            showBackButton = true,
            onBackClick = onNavigateBack
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome back! Sign in to continue.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                LoginFormField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    placeholder = "e.g. john@email.com",
                    errorMessage = emailError
                )

                Spacer(modifier = Modifier.height(12.dp))

                LoginFormField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    placeholder = "Enter your password",
                    errorMessage = passwordError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onPasswordVisibilityToggle) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = TextSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = onLoginClick,
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Login", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Register Now!")
                            }
                        },
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

package com.example.playscore.presentation.ui.screen.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.playscore.presentation.ui.screen.register.component.RegisterFormField
import com.example.playscore.presentation.util.Validation
import com.example.playscore.presentation.view_model.register.RegisterNavigationEvent
import com.example.playscore.presentation.view_model.register.RegisterUiState
import com.example.playscore.presentation.view_model.register.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                RegisterNavigationEvent.Navigate -> onRegisterSuccess()
            }
        }
    }

    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val usernameError by remember {
        derivedStateOf {
            if (username.isNotBlank() && !Validation.isUsernameValid(username)) {
                "Username cannot be empty!"
            } else ""
        }
    }

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
                "Password must be at least 6 characters long!"
            } else ""
        }
    }

    val confirmPasswordError by remember {
        derivedStateOf {
            if (confirmPassword.isNotBlank() && !Validation.doPasswordsMatch(password, confirmPassword)) {
                "Passwords do not match!"
            } else ""
        }
    }

    val isFormValid by remember {
        derivedStateOf {
            Validation.isUsernameValid(username) &&
                    Validation.isEmailValid(email) &&
                    Validation.isPasswordValid(password) &&
                    Validation.doPasswordsMatch(password, confirmPassword)
        }
    }

    RegisterScreen(
        username = username,
        onUsernameChange = { username = it },
        usernameError = usernameError,
        email = email,
        onEmailChange = { email = it },
        emailError = emailError,
        password = password,
        onPasswordChange = { password = it },
        passwordError = passwordError,
        passwordVisible = passwordVisible,
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        confirmPasswordError = confirmPasswordError,
        confirmPasswordVisible = confirmPasswordVisible,
        onConfirmPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
        isFormValid = isFormValid && uiState !is RegisterUiState.Loading,
        errorMessage = (uiState as? RegisterUiState.Error)?.message.orEmpty(),
        onNavigateToLogin = onNavigateToLogin,
        onNavigateBack = onNavigateBack,
        onRegisterClick = {
            viewModel.register(username, email, password)
        }
    )
}

@Composable
private fun RegisterScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    usernameError: String,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: String,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    isFormValid: Boolean,
    errorMessage: String,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Create Account",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign up to start tracking your game scores.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                RegisterFormField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = "Username",
                    placeholder = "e.g. John",
                    errorMessage = usernameError
                )

                Spacer(modifier = Modifier.height(12.dp))

                RegisterFormField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    placeholder = "e.g john@email.com",
                    errorMessage = emailError
                )

                Spacer(modifier = Modifier.height(12.dp))

                RegisterFormField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    placeholder = "Enter password",
                    errorMessage = passwordError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onPasswordVisibilityToggle) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                RegisterFormField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirm Password",
                    placeholder = "Repeat your password",
                    errorMessage = confirmPasswordError,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onConfirmPasswordVisibilityToggle) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null,
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
                    onClick = onRegisterClick,
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Register", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Already have an account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Login!")
                            }
                        },
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

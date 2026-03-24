package com.example.playscore.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.playscore.ui.screens.register.components.RegisterTextField
import com.example.playscore.ui.theme.primaryPurple


@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var confirmPassword by remember { mutableStateOf("") }

    val isPasswordValid = password.length >= 6
    val passwordsMatch = password == confirmPassword && password.isNotEmpty()
    val isFormValid = username.isNotBlank() && isPasswordValid && passwordsMatch

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = primaryPurple
        )

        Spacer(modifier = Modifier.height(32.dp))

        RegisterTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username"
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryPurple)
        ) {
            Text("Register", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {onNavigateToLogin()}) {
            Text("Already have an account? Log in", color = primaryPurple)
        }

        if(password.isNotEmpty() && password.length < 6) {
            Text("Password must be at least 6 characters long!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        } else if (confirmPassword.isNotEmpty() && !passwordsMatch) {
            Text("Passwords do not match!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
    }
}
package com.example.playscore.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.playscore.ui.screens.login.components.LoginTextField
import com.example.playscore.ui.theme.primaryPurple
import com.example.playscore.ui.theme.darkGray
import com.example.playscore.ui.theme.LightPurple

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFormValid = username.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge, color = primaryPurple)
        Spacer(modifier = Modifier.height(32.dp))

        LoginTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username"
        )
        Spacer(modifier = Modifier.height(16.dp))

        LoginTextField(
            value = password,
            onValueChange = { password = it},
            label = "Password"
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {}) {
                Text("Forgot Password?", color = primaryPurple)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(contentColor = primaryPurple)
        ){
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {onNavigateToRegister()}) {
            Row{
                Text("Don't have an account?", color = darkGray)
                Text("Sign Up", color = LightPurple)
            }
        }
        if(!isFormValid && (username.isNotEmpty() || password.isNotEmpty())) {
            Text(text = "Please enter both username and password", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
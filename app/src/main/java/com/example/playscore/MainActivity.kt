package com.example.playscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.playscore.ui.screens.login.LoginScreen
import com.example.playscore.ui.screens.register.RegisterScreen
import com.example.playscore.ui.theme.PlayScoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayScoreTheme {
                var currentScreen by remember { mutableStateOf("login") }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onNavigateToRegister = {
                                currentScreen = "register"
                            }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onNavigateToLogin = {
                                currentScreen = "login"
                            }
                        )
                    }
                }
            }
        }
    }
}
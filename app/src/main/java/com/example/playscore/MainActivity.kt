package com.example.playscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.playscore.presentation.theme.PlayScoreTheme
import com.example.playscore.presentation.ui.screen.creategame.CreateGameScreen
import com.example.playscore.presentation.ui.screen.home.HomeScreen
import com.example.playscore.presentation.ui.screen.login.LoginScreen
import com.example.playscore.presentation.ui.screen.register.RegisterScreen
import com.example.playscore.presentation.ui.screen.viewgame.ViewGameScreen
import com.example.playscore.presentation.view_model.PlayScoreViewModel

class MainActivity : ComponentActivity() {
    private val viewModel : PlayScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayScoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlayScoreApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun PlayScoreApp(viewModel : PlayScoreViewModel) {
    var currentScreen by remember { mutableStateOf("home") }

    when(currentScreen) {
        "home" -> HomeScreen(
            viewModel = viewModel,
            onNavigateToLogin = { currentScreen = "login" },
            onNavigateToCreateGame = { currentScreen = "createGame" },
            onNavigateToViewGame = { gameId ->
                viewModel.selectGame(gameId)
                currentScreen = "viewGame"
            }
        )
        "login" -> LoginScreen(
            onNavigateToRegister = { currentScreen = "register" },
            onNavigateBack = { currentScreen = "home" },
            onLoginSuccess = { currentScreen = "home" }
        )
        "register" -> RegisterScreen(
            onNavigateToLogin = { currentScreen = "login" },
            onNavigateBack = { currentScreen = "login" },
            onRegisterSuccess = { currentScreen = "login" }
        )
        "createGame" -> CreateGameScreen(
            onNavigateBack = { currentScreen = "home" },
            onGameCreated = { currentScreen = "home" }
        )
        "viewGame" -> ViewGameScreen(
            viewModel = viewModel,
            onNavigateBack = { currentScreen = "home" }
        )
    }
}
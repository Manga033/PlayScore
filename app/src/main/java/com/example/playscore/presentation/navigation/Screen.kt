package com.example.playscore.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object CreateGame : Screen("create_game")
    data object ViewGame : Screen("view_game/{gameId}/{gameName}") {
        fun createRoute(gameId: Int, gameName: String): String {
            return "view_game/$gameId/${Uri.encode(gameName)}"
        }
    }
}

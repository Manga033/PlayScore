package com.example.playscore.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.playscore.presentation.ui.component.BottomNavBar
import com.example.playscore.presentation.ui.screen.creategame.CreateGameScreen
import com.example.playscore.presentation.ui.screen.home.HomeScreen
import com.example.playscore.presentation.ui.screen.login.LoginScreen
import com.example.playscore.presentation.ui.screen.register.RegisterScreen
import com.example.playscore.presentation.ui.screen.viewgame.ViewGameScreen
import com.example.playscore.presentation.view_model.PlayScoreViewModel

@Composable
fun AppNavigation(viewModel: PlayScoreViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomBarScreens = listOf(Screen.Home.route, Screen.CreateGame.route)
    val showBottomBar = currentRoute in bottomBarScreens

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    gameCount = viewModel.totalGamesCount,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onNavigateToCreateGame = {
                        navController.navigate(Screen.CreateGame.route) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToViewGame = { gameId, gameName ->
                        viewModel.selectGame(gameId)
                        navController.navigate(Screen.ViewGame.createRoute(gameId, gameName))
                    }
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.navigateUp()
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.CreateGame.route) {
                CreateGameScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onGameCreated = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                )
            }
            composable(
                route = Screen.ViewGame.route,
                arguments = listOf(
                    navArgument("gameId") { type = NavType.IntType },
                    navArgument("gameName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                val gameName = backStackEntry.arguments?.getString("gameName") ?: ""

                ViewGameScreen(
                    viewModel = viewModel,
                    gameName = gameName,
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
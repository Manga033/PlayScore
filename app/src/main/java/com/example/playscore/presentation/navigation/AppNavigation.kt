package com.example.playscore.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.playscore.presentation.view_model.home.HomeUiState
import com.example.playscore.presentation.view_model.home.HomeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomBarScreens = listOf(Screen.Home.route, Screen.CreateGame.route)
    val showBottomBar = currentRoute in bottomBarScreens
    val gameCount = (homeUiState as? HomeUiState.Success)?.totalGames ?: 0

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    gameCount = gameCount,
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
                    viewModel = homeViewModel,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToViewGame = { gameId, gameName ->
                        navController.navigate(Screen.ViewGame.createRoute(gameId, gameName))
                    }
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onLoginSuccess = {
                        homeViewModel.refreshLoginSession()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = hiltViewModel(),
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
                    viewModel = hiltViewModel(),
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onGameCreated = { message ->
                        homeViewModel.showSyncMessage(message)
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
                val gameName = backStackEntry.arguments?.getString("gameName") ?: ""

                ViewGameScreen(
                    viewModel = hiltViewModel(),
                    gameName = gameName,
                    onNavigateBack = { message ->
                        if (message.isNotBlank()) {
                            homeViewModel.showSyncMessage(message)
                        }
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

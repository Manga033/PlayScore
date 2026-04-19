package com.example.playscore.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playscore.presentation.navigation.Screen
import com.example.playscore.presentation.theme.AccentPurple
import com.example.playscore.presentation.theme.DarkSurface
import com.example.playscore.presentation.theme.TextSecondary

@Composable
fun BottomNavBar(
    currentRoute: String?,
    gameCount: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToCreateGame: () -> Unit
) {
    NavigationBar(
        containerColor = DarkSurface
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = onNavigateToHome,
            icon = {
                BadgedBox(
                    badge = {
                        if (gameCount > 0) {
                            Badge(
                                containerColor = AccentPurple
                            ) {
                                Text(text = gameCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home"
                    )
                }
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentPurple,
                selectedTextColor = AccentPurple,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = AccentPurple.copy(alpha = 0.15f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.CreateGame.route,
            onClick = onNavigateToCreateGame,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Game"
                )
            },
            label = { Text("Create") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentPurple,
                selectedTextColor = AccentPurple,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = AccentPurple.copy(alpha = 0.15f)
            )
        )
    }
}
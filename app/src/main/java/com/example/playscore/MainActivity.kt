package com.yourname.gamescoretracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yourname.gamescoretracker.navigation.AppNavigation
import com.yourname.gamescoretracker.ui.theme.GameScoreTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScoreTrackerTheme {
                AppNavigation()
            }
        }
    }
}
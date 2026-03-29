package com.example.playscore.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PlayScoreColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    secondary = AccentPurple,
    tertiary = LightPurple,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed
)

@Composable
fun PlayScoreTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PlayScoreColorScheme,
        typography = Typography,
        content = content
    )
}
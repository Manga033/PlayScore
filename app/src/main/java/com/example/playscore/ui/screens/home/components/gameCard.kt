package com.example.playscore.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.playscore.data.model.Game
import com.example.playscore.ui.theme.darkGray

@Composable
fun GameCard(game : Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(darkGray)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column{
            Text(text = game.title, color = Color.White)
            Text(text = "Status: ${game.status}", color = Color.Gray)
        }
        Text(text = game.score, color = Color.White)
    }
}
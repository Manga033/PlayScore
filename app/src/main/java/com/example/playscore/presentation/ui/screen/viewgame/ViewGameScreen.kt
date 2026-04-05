package com.example.playscore.presentation.ui.screen.viewgame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playscore.presentation.theme.AccentPurple
import com.example.playscore.presentation.theme.DarkCard
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.component.ScreenHeader
import com.example.playscore.presentation.ui.screen.viewgame.component.PlayerScoreCard
import com.example.playscore.presentation.view_model.PlayScoreViewModel

@Composable
fun ViewGameScreen(
    viewModel: PlayScoreViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.viewGameUiState.value
    val game = uiState.game

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Game Details",
            showBackButton = true,
            onBackClick = onNavigateBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!uiState.hasGame) {
            Text(
                text = "No game selected.",
                fontSize = 14.sp,
                color = TextSecondary
            )
        } else {
            val game = uiState.game!!

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = game.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = game.type,
                            fontSize = 14.sp,
                            color = AccentPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = game.date,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = "Winner: ${uiState.winnerName}",
                            fontSize = 14.sp,
                            color = AccentPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Scoreboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            val sortedPlayers = game.players.sortedByDescending { it.score }
            val highestScore = sortedPlayers.firstOrNull()?.score ?: 0

            sortedPlayers.forEachIndexed { index, player ->
                PlayerScoreCard(
                    player = player,
                    isWinner = player.score == highestScore,
                    rank = index + 1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if(game.type == "Board Game") {
                Text(
                    text = "Total Players: ${uiState.playerCount}",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
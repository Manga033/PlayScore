package com.example.playscore.presentation.ui.screen.viewgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.playscore.presentation.ui.component.EmptyStateMessage
import com.example.playscore.presentation.ui.component.ScreenHeader
import com.example.playscore.presentation.ui.screen.viewgame.component.PlayerScoreCard
import com.example.playscore.presentation.view_model.PlayScoreViewModel

@Composable
fun ViewGameScreen(
    viewModel: PlayScoreViewModel,
    gameName: String,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.viewGameUiState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = gameName.ifBlank { "Game Details" },
            showBackButton = true,
            onBackClick = onNavigateBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!uiState.hasGame) {
            EmptyStateMessage(
                message = "Game not found",
                subtitle = "Please go back and select a game"
            )
        } else {
            val game = uiState.game!!
            val sortedPlayers = game.players.sortedByDescending { it.score }
            val highestScore = sortedPlayers.firstOrNull()?.score ?: 0

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
                        Text(text = game.type, fontSize = 14.sp, color = AccentPurple)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(text = game.date, fontSize = 14.sp, color = TextSecondary)
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

            if (game.type == "Board Game") {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Total Players: ${uiState.playerCount}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = sortedPlayers,
                    key = { _, player -> player.name }
                ) { index, player ->
                    PlayerScoreCard(
                        player = player,
                        isWinner = player.score == highestScore,
                        rank = index + 1
                    )
                }
            }
        }
    }
}
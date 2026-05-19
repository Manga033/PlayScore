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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import com.example.playscore.model.domain.ScoreLogEntry
import com.example.playscore.presentation.theme.AccentPurple
import com.example.playscore.presentation.theme.DarkCard
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.component.EmptyStateMessage
import com.example.playscore.presentation.ui.component.ScreenHeader
import com.example.playscore.presentation.ui.screen.viewgame.component.PlayerScoreCard
import com.example.playscore.presentation.view_model.viewgame.ViewGameNavigationEvent
import com.example.playscore.presentation.view_model.viewgame.ViewGameUiState
import com.example.playscore.presentation.view_model.viewgame.ViewGameViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ViewGameScreen(
    viewModel: ViewGameViewModel,
    gameName: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                ViewGameNavigationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    ViewGameScreen(
        gameName = gameName,
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onDecreaseScore = { player ->
            viewModel.decreaseScore(player.id, player.score)
        },
        onIncreaseScore = { player ->
            viewModel.increaseScore(player.id, player.score)
        },
        onDeleteGameClick = {
            viewModel.deleteGame()
        }
    )
}

@Composable
private fun ViewGameScreen(
    gameName: String,
    uiState: ViewGameUiState,
    onNavigateBack: () -> Unit,
    onDecreaseScore: (Player) -> Unit,
    onIncreaseScore: (Player) -> Unit,
    onDeleteGameClick: () -> Unit
) {
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

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

        when (uiState) {
            ViewGameUiState.Init,
            ViewGameUiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is ViewGameUiState.Error -> {
                EmptyStateMessage(
                    message = "Game not found",
                    subtitle = uiState.message
                )
            }
            is ViewGameUiState.Success -> {
                ViewGameContent(
                    game = uiState.game,
                    scoreLog = uiState.scoreLog,
                    winnerName = uiState.winnerName,
                    playerCount = uiState.playerCount,
                    totalScore = uiState.totalScore,
                    modifier = Modifier.weight(1f),
                    onDecreaseScore = onDecreaseScore,
                    onIncreaseScore = onIncreaseScore,
                    onDeleteGameClick = { showDeleteConfirmation = true }
                )
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(text = "Delete game?") },
            text = {
                Text(text = "This will permanently delete this game and its scores.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeleteGameClick()
                    }
                ) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@Composable
private fun ViewGameContent(
    game: Game,
    scoreLog: List<ScoreLogEntry>,
    winnerName: String,
    playerCount: Int,
    totalScore: Int,
    modifier: Modifier = Modifier,
    onDecreaseScore: (Player) -> Unit,
    onIncreaseScore: (Player) -> Unit,
    onDeleteGameClick: () -> Unit
) {
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
                    text = "Winner: $winnerName",
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
            text = "Total Players: $playerCount",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }

    Spacer(modifier = Modifier.height(2.dp))
    Text(
        text = "Total Points Played: $totalScore",
        fontSize = 12.sp,
        color = TextSecondary
    )

    Spacer(modifier = Modifier.height(8.dp))

    HorizontalDivider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        itemsIndexed(
            items = sortedPlayers,
            key = { index, player -> "${player.id}-$index" }
        ) { index, player ->
            PlayerScoreCard(
                player = player,
                isWinner = player.score == highestScore,
                rank = index + 1,
                onDecreaseScore = { onDecreaseScore(player) },
                onIncreaseScore = { onIncreaseScore(player) }
            )
        }
        item {
            ScoreLogSection(
                scoreLog = scoreLog,
                players = game.players,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        item {
            Button(
                onClick = onDeleteGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Delete Game")
            }
        }
    }
}

@Composable
private fun ScoreLogSection(
    scoreLog: List<ScoreLogEntry>,
    players: List<Player>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Score Log",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (scoreLog.isEmpty()) {
            Text(
                text = "No score changes yet.",
                fontSize = 14.sp,
                color = TextSecondary
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                scoreLog.take(10).forEach { entry ->
                    ScoreLogRow(
                        entry = entry,
                        playerName = players.find { it.id == entry.playerId }?.name ?: "Unknown player"
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreLogRow(
    entry: ScoreLogEntry,
    playerName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$playerName updated score",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = formatScoreLogTime(entry.createdAt),
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
        Text(
            text = "${entry.score} pts",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AccentPurple
        )
    }
}

private fun formatScoreLogTime(timestamp: Long): String {
    return SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(timestamp))
}

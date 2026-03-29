package com.example.playscore.presentation.ui.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.playscore.data.repository.Game
import com.example.playscore.presentation.theme.AccentPurple
import com.example.playscore.presentation.theme.DarkCard
import com.example.playscore.presentation.theme.TextSecondary

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    val winner = game.players.maxByOrNull { it.score }

    val scoreText = if (game.players.size == 2) {
        val p1 = game.players[0]
        val p2 = game.players[1]
        "${p1.name} (${p1.score}) vs ${p2.name} (${p2.score})"
    } else {
        val others = game.players
            .sortedByDescending { it.score }
            .drop(1)
            .joinToString(" · ") { "${it.name} (${it.score})" }
        "Winner: ${winner?.name ?: "TBD"} (${winner?.score ?: 0})  ·  $others"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = game.type,
                    fontSize = 12.sp,
                    color = AccentPurple,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Score",
                    tint = AccentPurple,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = scoreText,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = game.date,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
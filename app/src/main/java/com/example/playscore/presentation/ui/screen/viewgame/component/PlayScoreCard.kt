package com.example.playscore.presentation.ui.screen.viewgame.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.playscore.data.repository.Player
import com.example.playscore.presentation.theme.AccentPurple
import com.example.playscore.presentation.theme.DarkCard
import com.example.playscore.presentation.theme.TextSecondary

@Composable
fun PlayerScoreCard(
    player: Player,
    isWinner: Boolean,
    rank: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner) AccentPurple.copy(alpha = 0.2f) else DarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isWinner) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Winner",
                        tint = AccentPurple,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    Text(
                        text = "#$rank",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = player.name,
                    fontSize = 16.sp,
                    fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = player.score.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWinner) AccentPurple else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
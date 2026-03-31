package com.example.playscore.presentation.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.screen.home.component.GameCard
import com.example.playscore.presentation.view_model.PlayScoreViewModel

@Composable
fun HomeScreen(
    viewModel: PlayScoreViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToCreateGame: () -> Unit,
    onNavigateToViewGame: (Int) -> Unit
) {
    val uiState = viewModel.homeUiState.value

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PlayScore",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            IconButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Login",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = "Track scores for your board games and sports matches.",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Recent Games",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            if (uiState.games.isEmpty()) {
                Text(
                    text = "No games yet. Create your first game!",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            } else {
                uiState.games.forEach { game ->
                    GameCard(
                        game = game,
                        onClick = { onNavigateToViewGame(game.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Button(
            onClick = onNavigateToCreateGame,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Create Game", fontSize = 16.sp)
        }
    }
}
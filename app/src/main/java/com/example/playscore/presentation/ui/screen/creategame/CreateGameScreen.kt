package com.example.playscore.presentation.ui.screen.creategame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.component.ScreenHeader
import com.example.playscore.presentation.ui.screen.creategame.component.CreateGameFormField
import com.example.playscore.presentation.util.Validation

@Composable
fun CreateGameScreen(
    onNavigateBack: () -> Unit,
    onGameCreated: () -> Unit
) {
    var gameName by remember { mutableStateOf("") }
    var gameType by remember { mutableStateOf("Board Game") }
    var player1 by remember { mutableStateOf("") }
    var player2 by remember { mutableStateOf("") }
    var player3 by remember { mutableStateOf("") }
    var player4 by remember { mutableStateOf("") }
    var gameNameError by remember { mutableStateOf("") }
    var player1Error by remember { mutableStateOf("") }

    val gameTypes = listOf("Board Game", "Sports")
    val isFormValid = Validation.isGameNameValid(gameName) && player1.isNotBlank()

    val isBoardGame = gameType == "Board Game"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScreenHeader(
            title = "New Game",
            showBackButton = true,
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Set up a new game to track scores.",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            CreateGameFormField(
                value = gameName,
                onValueChange = {
                    gameName = it
                    gameNameError = if (it.isBlank()) "Game name cannot be empty!" else ""
                },
                label = "Game Name",
                placeholder = "e.g. Football Match",
                errorMessage = gameNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Game Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            gameTypes.forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = gameType == type,
                        onClick = { gameType = type },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = type,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isBoardGame) "Players" else "Teams",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            CreateGameFormField(
                value = player1,
                onValueChange = {
                    player1 = it
                    player1Error = if (it.isBlank()) "At least one player is required" else ""
                },
                label = if (isBoardGame) "Player 1 *" else "Team 1 *",
                placeholder = if (isBoardGame) "Player name" else "Team name",
                errorMessage = player1Error
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateGameFormField(
                value = player2,
                onValueChange = { player2 = it },
                label = if (isBoardGame) "Player 2 (optional)" else "Team 2 (optional)",
                placeholder = if (isBoardGame) "Player name" else "Team name"
            )

            if (isBoardGame) {
                Spacer(modifier = Modifier.height(12.dp))

                CreateGameFormField(
                    value = player3,
                    onValueChange = { player3 = it },
                    label = "Player 3 (optional)",
                    placeholder = "Player name"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CreateGameFormField(
                    value = player4,
                    onValueChange = { player4 = it },
                    label = "Player 4 (optional)",
                    placeholder = "Player name"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Button(
            onClick = onGameCreated,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Create Game", fontSize = 16.sp)
        }
    }
}
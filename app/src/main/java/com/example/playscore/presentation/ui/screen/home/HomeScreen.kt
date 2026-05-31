package com.example.playscore.presentation.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playscore.presentation.theme.TextSecondary
import com.example.playscore.presentation.ui.component.EmptyStateMessage
import com.example.playscore.presentation.ui.component.ScrollToTopButton
import com.example.playscore.presentation.ui.screen.home.component.GameCard
import com.example.playscore.presentation.ui.screen.home.component.GameTypeCard
import com.example.playscore.presentation.ui.screen.home.component.RecentGameCard
import com.example.playscore.presentation.view_model.home.HomeUiState
import com.example.playscore.presentation.view_model.home.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToViewGame: (Int, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        HomeUiState.Init,
        HomeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is HomeUiState.Error -> {
            EmptyStateMessage(
                message = "Could not load games",
                subtitle = state.message
            )
        }
        is HomeUiState.Success -> {
            HomeScreen(
                uiState = state,
                onGameTypeSelected = viewModel::selectGameType,
                onNavigateToLogin = onNavigateToLogin,
                onLogout = viewModel::logout,
                onNavigateToViewGame = onNavigateToViewGame
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState.Success,
    onGameTypeSelected: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToViewGame: (Int, String) -> Unit
) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var sortAscending by rememberSaveable { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 2 }
    }

    val filteredGames by remember(uiState, searchQuery, sortAscending) {
        derivedStateOf {
            val typeFiltered = uiState.filteredByType
            val searched = if (searchQuery.isBlank()) typeFiltered
            else typeFiltered.filter { game ->
                game.name.contains(searchQuery, ignoreCase = true) ||
                        game.type.contains(searchQuery, ignoreCase = true)
            }
            if (sortAscending) searched.sortedBy { it.id }
            else searched.sortedByDescending { it.id }
        }
    }

    val isSearchEmpty = filteredGames.isEmpty() && uiState.hasGames

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            item {
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
                        onClick = {
                            if (uiState.isLoggedIn) onLogout() else onNavigateToLogin()
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = if (uiState.isLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.Default.Person,
                            contentDescription = if (uiState.isLoggedIn) "Logout" else "Login",
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
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search by game or type...",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = TextSecondary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.hasGames) {
                item {
                    Text(
                        text = "${uiState.totalGames} games tracked  -  " +
                                "${uiState.boardGames.size} board  -  " +
                                "${uiState.sportsGames.size} sports",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.networkMessage.isNotBlank() || uiState.cloudMessage.isNotBlank()) {
                item {
                    Text(
                        text = "API: ${uiState.networkGames.size} games  -  Cloud: ${uiState.cloudGames.size} games",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.syncMessage.isNotBlank()) {
                item {
                    Text(
                        text = uiState.syncMessage,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Text(
                    text = "Filter by Type",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.gameTypes) { type ->
                        GameTypeCard(
                            type = type,
                            count = uiState.countForType(type),
                            isSelected = uiState.selectedType == type,
                            onClick = { onGameTypeSelected(type) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (uiState.recentGames.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Games",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.recentGames,
                            key = { game -> game.id }
                        ) { game ->
                            RecentGameCard(
                                game = game,
                                onClick = { onNavigateToViewGame(game.id, game.name) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            stickyHeader(key = "allGamesHeader") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "All Games",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.hasGames) {
                                Text(
                                    text = "${filteredGames.size} results",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            IconButton(onClick = { sortAscending = !sortAscending }) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = if (sortAscending) "Sort newest first" else "Sort oldest first",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            when {
                !uiState.hasGames -> {
                    item {
                        EmptyStateMessage(
                            message = "No games yet",
                            subtitle = "Create your first game to start tracking scores!"
                        )
                    }
                }
                isSearchEmpty -> {
                    item {
                        EmptyStateMessage(
                            message = "No results found",
                            subtitle = "No games match \"$searchQuery\". Try a different search."
                        )
                    }
                }
                else -> {
                    items(
                        items = filteredGames,
                        key = { game -> game.id }
                    ) { game ->
                        GameCard(
                            game = game,
                            onClick = { onNavigateToViewGame(game.id, game.name) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        if (showScrollToTop) {
            ScrollToTopButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

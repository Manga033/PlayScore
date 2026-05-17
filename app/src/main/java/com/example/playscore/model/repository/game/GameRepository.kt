package com.example.playscore.model.repository.game

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import com.example.playscore.model.domain.ScoreLogEntry
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeGames(): Flow<List<Game>>
    fun observeGameById(id: Int): Flow<Game?>
    fun observeScoreHistoryForGame(gameId: Int): Flow<List<ScoreLogEntry>>
    fun observeGameTypes(): Flow<List<String>>
    suspend fun seedGamesIfNeeded()
    suspend fun createGame(name: String, type: String, players: List<Player>)
    suspend fun updateGame(game: Game)
    suspend fun updatePlayerScore(playerId: Int, score: Int)
    suspend fun deleteGame(game: Game)
    suspend fun deleteGameById(id: Int)
}


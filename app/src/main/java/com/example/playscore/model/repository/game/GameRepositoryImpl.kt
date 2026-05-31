package com.example.playscore.model.repository.game

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import com.example.playscore.model.data.local.dao.GameDao
import com.example.playscore.model.data.local.dao.GameTypeDao
import com.example.playscore.model.data.local.dao.PlayerDao
import com.example.playscore.model.data.local.dao.ScoreHistoryDao
import com.example.playscore.model.data.local.entity.GameTypeEntity
import com.example.playscore.model.data.local.entity.ScoreHistoryEntity
import com.example.playscore.model.domain.ScoreLogEntry
import com.example.playscore.model.repository.mappers.toGame
import com.example.playscore.model.repository.mappers.toGameEntity
import com.example.playscore.model.repository.mappers.toPlayerEntity
import com.example.playscore.model.data.local.util.GameSeed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val playerDao: PlayerDao,
    private val gameTypeDao: GameTypeDao,
    private val scoreHistoryDao: ScoreHistoryDao
) : GameRepository {

    override fun observeGames(): Flow<List<Game>> {
        return combine(
            gameDao.observeGamesWithPlayers(),
            gameTypeDao.observeGameTypes()
        ) { games, types ->
            games.map { game ->
                game.toGame(types.find { it.id == game.game.typeId })
            }
        }
    }

    override fun observeGameById(id: Int): Flow<Game?> {
        return combine(
            gameDao.observeGameWithPlayers(id),
            gameTypeDao.observeGameTypes()
        ) { game, types ->
            game?.toGame(types.find { it.id == game.game.typeId })
        }
    }

    override fun observeGameTypes(): Flow<List<String>> {
        return gameTypeDao.observeGameTypes().map { types ->
            listOf("All") + types.map { it.name }
        }
    }

    override fun observeScoreHistoryForGame(gameId: Int): Flow<List<ScoreLogEntry>> {
        return scoreHistoryDao.observeHistoryForGame(gameId).map { history ->
            history.map { entry ->
                ScoreLogEntry(
                    id = entry.id,
                    gameId = entry.gameId,
                    playerId = entry.playerId,
                    score = entry.score,
                    createdAt = entry.createdAt
                )
            }
        }
    }

    override suspend fun seedGamesIfNeeded() {
        withContext(Dispatchers.IO) {
            if (gameTypeDao.getGameTypeCount() == 0) {
                gameTypeDao.insertGameTypes(GameSeed.gameTypes.map { GameTypeEntity(name = it) })
            }
            if (gameDao.getGameCount() == 0) {
                GameSeed.defaultGames.forEach { game ->
                    insertGameWithPlayers(game)
                }
            }
        }
    }

    override suspend fun createGame(name: String, type: String, players: List<Player>): Int {
        return withContext(Dispatchers.IO) {
            val date = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
            insertGameWithPlayers(
                Game(
                    id = 0,
                    name = name,
                    type = type,
                    players = players,
                    date = date
                )
            )
        }
    }

    override suspend fun updateGame(game: Game) {
        withContext(Dispatchers.IO) {
            val typeId = getOrCreateGameTypeId(game.type)
            gameDao.updateGame(game.toGameEntity(typeId))
        }
    }

    override suspend fun updatePlayerScore(playerId: Int, score: Int) {
        withContext(Dispatchers.IO) {
            val player = playerDao.getPlayerById(playerId) ?: return@withContext
            playerDao.updatePlayerScore(playerId, score)
            scoreHistoryDao.insertScoreHistory(
                ScoreHistoryEntity(
                    gameId = player.gameId,
                    playerId = playerId,
                    score = score,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun deleteGame(game: Game) {
        withContext(Dispatchers.IO) {
            gameDao.deleteGame(game.toGameEntity(getOrCreateGameTypeId(game.type)))
        }
    }

    override suspend fun deleteGameById(id: Int) {
        withContext(Dispatchers.IO) {
            gameDao.deleteGameById(id)
        }
    }

    private suspend fun insertGameWithPlayers(game: Game): Int {
        val typeId = getOrCreateGameTypeId(game.type)
        val gameId = gameDao.insertGame(game.toGameEntity(typeId)).toInt()
        playerDao.insertPlayers(game.players.map { it.toPlayerEntity(gameId) })
        return gameId
    }

    private suspend fun getOrCreateGameTypeId(type: String): Int {
        val existingType = gameTypeDao.getGameTypeByName(type)
        if (existingType != null) return existingType.id
        return gameTypeDao.insertGameType(GameTypeEntity(name = type)).toInt()
    }
}


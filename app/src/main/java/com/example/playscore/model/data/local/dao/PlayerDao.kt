package com.example.playscore.model.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playscore.model.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players ORDER BY name ASC")
    fun observePlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE gameId = :gameId ORDER BY score DESC")
    fun observePlayersForGame(gameId: Int): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :id LIMIT 1")
    suspend fun getPlayerById(id: Int): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Query("UPDATE players SET score = :score WHERE id = :playerId")
    suspend fun updatePlayerScore(playerId: Int, score: Int)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)
}


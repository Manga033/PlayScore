package com.example.playscore.model.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playscore.model.data.local.entity.GameEntity
import com.example.playscore.model.data.local.entity.GameWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Transaction
    @Query("SELECT * FROM games ORDER BY id DESC")
    fun observeGamesWithPlayers(): Flow<List<GameWithPlayers>>

    @Transaction
    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    fun observeGameWithPlayers(id: Int): Flow<GameWithPlayers?>

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getGameCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Update
    suspend fun updateGame(game: GameEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGameById(id: Int)
}


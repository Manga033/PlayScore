package com.example.playscore.model.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playscore.model.data.local.entity.GameTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameTypeDao {
    @Query("SELECT * FROM game_types ORDER BY name ASC")
    fun observeGameTypes(): Flow<List<GameTypeEntity>>

    @Query("SELECT * FROM game_types WHERE name = :name LIMIT 1")
    suspend fun getGameTypeByName(name: String): GameTypeEntity?

    @Query("SELECT * FROM game_types WHERE id = :id LIMIT 1")
    suspend fun getGameTypeById(id: Int): GameTypeEntity?

    @Query("SELECT COUNT(*) FROM game_types")
    suspend fun getGameTypeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameType(gameType: GameTypeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameTypes(gameTypes: List<GameTypeEntity>)

    @Update
    suspend fun updateGameType(gameType: GameTypeEntity)

    @Delete
    suspend fun deleteGameType(gameType: GameTypeEntity)
}


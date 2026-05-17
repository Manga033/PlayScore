package com.example.playscore.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playscore.model.data.local.entity.ScoreHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreHistoryDao {
    @Query("SELECT * FROM score_history WHERE gameId = :gameId ORDER BY createdAt DESC")
    fun observeHistoryForGame(gameId: Int): Flow<List<ScoreHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreHistory(scoreHistory: ScoreHistoryEntity): Long
}

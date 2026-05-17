package com.example.playscore.model.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playscore.model.data.local.dao.GameDao
import com.example.playscore.model.data.local.dao.GameTypeDao
import com.example.playscore.model.data.local.dao.PlayerDao
import com.example.playscore.model.data.local.dao.ScoreHistoryDao
import com.example.playscore.model.data.local.dao.UserDao
import com.example.playscore.model.data.local.entity.GameEntity
import com.example.playscore.model.data.local.entity.GameTypeEntity
import com.example.playscore.model.data.local.entity.PlayerEntity
import com.example.playscore.model.data.local.entity.ScoreHistoryEntity
import com.example.playscore.model.data.local.entity.UserEntity

@Database(
    entities = [
        GameEntity::class,
        PlayerEntity::class,
        GameTypeEntity::class,
        UserEntity::class,
        ScoreHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
    abstract fun gameTypeDao(): GameTypeDao
    abstract fun userDao(): UserDao
    abstract fun scoreHistoryDao(): ScoreHistoryDao
}


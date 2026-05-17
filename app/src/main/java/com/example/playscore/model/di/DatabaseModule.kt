package com.example.playscore.model.di

import android.content.Context
import androidx.room.Room
import com.example.playscore.model.data.local.dao.GameDao
import com.example.playscore.model.data.local.dao.GameTypeDao
import com.example.playscore.model.data.local.dao.PlayerDao
import com.example.playscore.model.data.local.dao.ScoreHistoryDao
import com.example.playscore.model.data.local.dao.UserDao
import com.example.playscore.model.data.local.db.AppDatabase
import com.example.playscore.model.repository.game.GameRepository
import com.example.playscore.model.repository.game.GameRepositoryImpl
import com.example.playscore.model.repository.user.UserRepository
import com.example.playscore.model.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "playscore_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideGameDao(database: AppDatabase): GameDao = database.gameDao()

    @Provides
    @Singleton
    fun providePlayerDao(database: AppDatabase): PlayerDao = database.playerDao()

    @Provides
    @Singleton
    fun provideGameTypeDao(database: AppDatabase): GameTypeDao = database.gameTypeDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideScoreHistoryDao(database: AppDatabase): ScoreHistoryDao = database.scoreHistoryDao()

    @Provides
    @Singleton
    fun provideGameRepository(
        gameDao: GameDao,
        playerDao: PlayerDao,
        gameTypeDao: GameTypeDao,
        scoreHistoryDao: ScoreHistoryDao
    ): GameRepository {
        return GameRepositoryImpl(gameDao, playerDao, gameTypeDao, scoreHistoryDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }
}


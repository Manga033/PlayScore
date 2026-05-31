package com.example.playscore.model.repository.cloud

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import kotlinx.coroutines.flow.Flow

interface CloudGameRepository {
    fun observeCloudGames(): Flow<List<Game>>
    suspend fun addGame(localId: Int, name: String, type: String, players: List<Player>, date: String)
}

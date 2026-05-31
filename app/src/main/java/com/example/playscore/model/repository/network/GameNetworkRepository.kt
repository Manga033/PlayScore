package com.example.playscore.model.repository.network

import com.example.playscore.model.datasource.network.dto.CreateGameDto
import com.example.playscore.model.datasource.network.dto.GameDto
import com.example.playscore.model.datasource.network.dto.UpdateGameDto

interface GameNetworkRepository {
    suspend fun getGames(): List<GameDto>
    suspend fun getGameById(id: Int): GameDto
    suspend fun createGame(game: CreateGameDto): GameDto
    suspend fun updateGame(id: Int, game: UpdateGameDto): GameDto
    suspend fun deleteGame(id: Int)
}

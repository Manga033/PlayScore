package com.example.playscore.model.repository.network

import com.example.playscore.model.datasource.network.dto.CreateGameDto
import com.example.playscore.model.datasource.network.dto.GameDto
import com.example.playscore.model.datasource.network.dto.UpdateGameDto
import com.example.playscore.model.datasource.network.service.GameApiService
import javax.inject.Inject

class GameNetworkRepositoryImpl @Inject constructor(
    private val api: GameApiService
) : GameNetworkRepository {

    override suspend fun getGames(): List<GameDto> = api.getGames()

    override suspend fun getGameById(id: Int): GameDto = api.getGameById(id)

    override suspend fun createGame(game: CreateGameDto): GameDto =
        api.createGame(game)

    override suspend fun updateGame(id: Int, game: UpdateGameDto): GameDto =
        api.updateGame(id, game)

    override suspend fun deleteGame(id: Int) =
        api.deleteGame(id)
}

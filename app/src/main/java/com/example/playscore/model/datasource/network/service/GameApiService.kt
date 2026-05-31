package com.example.playscore.model.datasource.network.service

import com.example.playscore.model.datasource.network.dto.CreateGameDto
import com.example.playscore.model.datasource.network.dto.GameDto
import com.example.playscore.model.datasource.network.dto.UpdateGameDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GameApiService {

    @GET("games/")
    suspend fun getGames(
        @Header("X-Authentication") authHeader: String = "yes"
    ): List<GameDto>

    @GET("games/{id}")
    suspend fun getGameById(
        @Path("id") id: Int,
        @Header("X-Authentication") authHeader: String = "yes"
    ): GameDto

    @POST("games/")
    suspend fun createGame(
        @Body game: CreateGameDto,
        @Header("X-Authentication") authHeader: String = "yes"
    ): GameDto

    @PUT("games/{id}")
    suspend fun updateGame(
        @Path("id") id: Int,
        @Body game: UpdateGameDto,
        @Header("X-Authentication") authHeader: String = "yes"
    ): GameDto

    @DELETE("games/{id}")
    suspend fun deleteGame(
        @Path("id") id: Int,
        @Header("X-Authentication") authHeader: String = "yes"
    )
}

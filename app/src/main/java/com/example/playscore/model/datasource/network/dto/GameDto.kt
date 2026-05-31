package com.example.playscore.model.datasource.network.dto

data class GameDto(
    val id: Int,
    val name: String,
    val type: String,
    val date: String
)

data class CreateGameDto(
    val name: String,
    val type: String,
    val date: String
)

data class UpdateGameDto(
    val name: String? = null,
    val type: String? = null,
    val date: String? = null
)

package com.example.playscore.model.domain

data class Player(
    val id: Int = 0,
    val name: String,
    val score: Int
)

data class Game(
    val id: Int,
    val name: String,
    val type: String,
    val players: List<Player>,
    val date: String
)

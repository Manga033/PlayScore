package com.example.playscore.data.util

data class Player(
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
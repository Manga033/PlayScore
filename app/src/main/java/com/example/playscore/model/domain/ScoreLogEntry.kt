package com.example.playscore.model.domain

data class ScoreLogEntry(
    val id: Int,
    val gameId: Int,
    val playerId: Int,
    val score: Int,
    val createdAt: Long
)

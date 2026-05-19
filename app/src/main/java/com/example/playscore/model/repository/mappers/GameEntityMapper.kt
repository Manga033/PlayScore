package com.example.playscore.model.repository.mappers

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import com.example.playscore.model.data.local.entity.GameEntity
import com.example.playscore.model.data.local.entity.GameTypeEntity
import com.example.playscore.model.data.local.entity.GameWithPlayers
import com.example.playscore.model.data.local.entity.PlayerEntity

fun GameWithPlayers.toGame(type: GameTypeEntity?): Game {
    return Game(
        id = game.id,
        name = game.name,
        type = type?.name ?: "Unknown",
        players = players.map { it.toPlayer() },
        date = game.date
    )
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        id = id,
        name = name,
        score = score
    )
}

fun Game.toGameEntity(typeId: Int): GameEntity {
    return GameEntity(
        id = id,
        name = name,
        typeId = typeId,
        date = date
    )
}

fun Player.toPlayerEntity(gameId: Int): PlayerEntity {
    return PlayerEntity(
        id = id,
        gameId = gameId,
        name = name,
        score = score
    )
}


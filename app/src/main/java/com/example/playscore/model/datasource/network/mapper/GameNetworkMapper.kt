package com.example.playscore.model.datasource.network.mapper

import com.example.playscore.model.datasource.network.dto.GameDto
import com.example.playscore.model.domain.Game

fun GameDto.toGame(): Game {
    return Game(
        id = id,
        name = name,
        type = type,
        players = emptyList(),
        date = date
    )
}

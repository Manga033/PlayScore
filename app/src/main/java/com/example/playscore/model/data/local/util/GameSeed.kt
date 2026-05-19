package com.example.playscore.model.data.local.util

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player

object GameSeed {
    val gameTypes = listOf("Football", "Basketball", "Tennis", "Board Game", "Sports")

    val defaultGames = listOf(
        Game(
            id = 1,
            name = "World Cup Qualifiers Semi-Final",
            type = "Football",
            players = listOf(
                Player(name = "Bosnia and Herzegovina", score = 1),
                Player(name = "Wales", score = 1)
            ),
            date = "26 Mar, 2026"
        ),
        Game(
            id = 2,
            name = "Europaleague",
            type = "Basketball",
            players = listOf(
                Player(name = "Barcelona", score = 88),
                Player(name = "Monaco", score = 74)
            ),
            date = "22 Mar 2026"
        ),
        Game(
            id = 3,
            name = "Australian Open",
            type = "Tennis",
            players = listOf(
                Player(name = "Novak Djokovic", score = 1),
                Player(name = "Carlos Alcaraz", score = 3)
            ),
            date = "01 Feb, 2026"
        ),
        Game(
            id = 4,
            name = "Uno Championship",
            type = "Board Game",
            players = listOf(
                Player(name = "Imran", score = 450),
                Player(name = "Danin", score = 380),
                Player(name = "Dino", score = 210)
            ),
            date = "Mar 26, 2026"
        ),
        Game(
            id = 5,
            name = "Chess Final",
            type = "Board Game",
            players = listOf(
                Player(name = "Magnus Carlsen", score = 12),
                Player(name = "Hikaru Nakamura", score = 8)
            ),
            date = "10 Apr, 2026"
        ),
        Game(
            id = 6,
            name = "NBA Playoffs",
            type = "Basketball",
            players = listOf(
                Player(name = "LA Lakers", score = 112),
                Player(name = "Boston Celtics", score = 108)
            ),
            date = "15 Apr, 2026"
        )
    )
}


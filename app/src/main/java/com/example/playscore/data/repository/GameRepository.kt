package com.example.playscore.data.repository

import com.example.playscore.data.util.Game
import com.example.playscore.data.util.Player

object GameRepository {
    fun getGames() : List<Game> {
        return listOf(
            Game(
                id = 1,
                name = "World Cup Qualifiers Semi-Final",
                type = "Football",
                players = listOf(
                    Player("Bosnia and Herzegovina", 1),
                    Player("Wales", 1)
                ),
                date = "26 Mar, 2026"
            ),
            Game(
                id = 2,
                name = "Europaleague",
                type = "Basketball",
                players = listOf(
                    Player("Barcelona", 88),
                    Player("Monaco", 74)
                ),
                date = "22 Mar 2026"
            ),
            Game(
                id = 3,
                name = "Australian Open",
                type = "Tennis",
                players = listOf(
                    Player("Novak Djokovic", 1),
                    Player("Carlos Alcaraz", 3)
                ),
                date = "01 Feb, 2026"
            ),
            Game(
                id = 4,
                name = "Uno Championship",
                type = "Board Game",
                players = listOf(
                    Player("Imran", 450),
                    Player("Danin", 380),
                    Player("Dino", 210)
                ),
                date = "Mar 26, 2026"
            ),
            Game(
                id = 5,
                name = "Chess Final",
                type = "Board Game",
                players = listOf(
                    Player("Magnus Carlsen", 12),
                    Player("Hikaru Nakamura", 8)
                ),
                date = "10 Apr, 2026"
            ),
            Game(
                id = 6,
                name = "NBA Playoffs",
                type = "Basketball",
                players = listOf(
                    Player("LA Lakers", 112),
                    Player("Boston Celtics", 108)
                ),
                date = "15 Apr, 2026"
            )
        )
    }

    fun getGameById(id : Int): Game? {
        return getGames().find { it.id == id }
    }

    fun getGameTypes(): List<String> {
        return listOf("All", "Football", "Basketball", "Tennis", "Board Game")
    }
}


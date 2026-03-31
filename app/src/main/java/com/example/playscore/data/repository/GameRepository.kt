package com.example.playscore.data.repository

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
            )
        )
    }
}


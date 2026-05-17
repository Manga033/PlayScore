package com.example.playscore.presentation.util

object Validation {
    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && email.contains("@")
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun isGameNameValid(gameName: String): Boolean {
        return gameName.isNotBlank()
    }

    fun isPlayerNameValid(playerName: String): Boolean {
        return playerName.isNotBlank()
    }

    fun hasRequiredPlayersForGameType(gameType: String, playerNames: List<String>): Boolean {
        val validPlayerCount = playerNames.count { isPlayerNameValid(it) }
        return if (gameType == "Sports") validPlayerCount >= 2 else validPlayerCount >= 1
    }
}

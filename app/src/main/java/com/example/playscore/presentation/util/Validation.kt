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
}
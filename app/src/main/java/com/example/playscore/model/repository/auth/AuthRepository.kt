package com.example.playscore.model.repository.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun register(email: String, password: String)
    suspend fun login(email: String, password: String)
    suspend fun signInWithGoogle(idToken: String): FirebaseUser?
    fun logout()
    fun getCurrentUserId(): String?
    fun isLoggedIn(): Boolean
}

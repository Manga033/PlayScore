package com.example.playscore.model.repository.user

import com.example.playscore.model.data.local.entity.UserEntity

interface UserRepository {
    suspend fun login(email: String, password: String): UserEntity?
    suspend fun register(username: String, email: String, password: String): Boolean
}


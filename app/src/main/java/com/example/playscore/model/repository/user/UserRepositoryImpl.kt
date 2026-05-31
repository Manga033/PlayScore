package com.example.playscore.model.repository.user

import com.example.playscore.model.data.local.dao.UserDao
import com.example.playscore.model.data.local.entity.UserEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun login(email: String, password: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmailAndPassword(email, password)
        }
    }

    override suspend fun isEmailRegistered(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email) != null
        }
    }

    override suspend fun register(username: String, email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            if (userDao.getUserByEmail(email) != null) {
                false
            } else {
                userDao.insertUser(
                    UserEntity(
                        username = username,
                        email = email,
                        password = password
                    )
                )
                true
            }
        }
    }
}


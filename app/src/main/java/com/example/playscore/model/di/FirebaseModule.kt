package com.example.playscore.model.di

import com.example.playscore.model.repository.auth.AuthRepository
import com.example.playscore.model.repository.auth.AuthRepositoryImpl
import com.example.playscore.model.repository.cloud.CloudGameRepository
import com.example.playscore.model.repository.cloud.CloudGameRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideCloudGameRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): CloudGameRepository {
        return CloudGameRepositoryImpl(firestore, firebaseAuth)
    }
}

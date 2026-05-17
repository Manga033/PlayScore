package com.example.playscore.model.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_types")
data class GameTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)


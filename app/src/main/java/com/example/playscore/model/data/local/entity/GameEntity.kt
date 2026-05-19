package com.example.playscore.model.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "games",
    foreignKeys = [
        ForeignKey(
            entity = GameTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["typeId"])]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val typeId: Int,
    val date: String
)


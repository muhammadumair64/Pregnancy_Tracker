package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "babies_names")
data class BabiesNameModel(
    @PrimaryKey val id: Int,
    val name: String,
    val meaning: String,
    val country: String,
    val type: String,
    val isFavorite: Boolean
)

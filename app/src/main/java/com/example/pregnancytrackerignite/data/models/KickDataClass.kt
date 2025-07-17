package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kickmodel")
data class KickDataClass (@PrimaryKey(autoGenerate = true) val id: Long = 0, var kickCount: String = "", var date: String = "")
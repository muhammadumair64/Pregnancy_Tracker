package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanmodel")
data class ScanDataClass (@PrimaryKey(autoGenerate = true) val id: Long = 0, var filePath: String = "", var week: Int)
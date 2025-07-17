package com.example.pregnancytrackerignite.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity
data class ContractionHistoryModel(
    val date: Date,
    val duration: DurationModel,
    val interval: DurationModel,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
@Parcelize
data class DurationModel(val hours: Long, val minutes: Long, val seconds: Long):Parcelable
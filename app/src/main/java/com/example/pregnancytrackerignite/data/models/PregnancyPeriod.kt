package com.example.pregnancytrackerignite.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity
data class PregnancyPeriod(
    val lastPeriodDate: Long?,
    val gestationalAge: GestationalAge?,
    val estimatedDate: Long?,
    val enteringDate: Date = Date(),
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
)

@Parcelize
data class GestationalAge(
    val weeks: Int, val days: Int
) : Parcelable
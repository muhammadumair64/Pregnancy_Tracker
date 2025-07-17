package com.example.pregnancytrackerignite.data.converters

import androidx.room.TypeConverter
import com.example.pregnancytrackerignite.data.models.DurationModel

class DurationModelConverter {
    @TypeConverter
    fun fromDuration(duration: DurationModel): String {
        return "${duration.hours}:${duration.minutes}:${duration.seconds}"
    }

    @TypeConverter
    fun toDuration(durationString: String): DurationModel {
        val parts = durationString.split(":")
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()
        return DurationModel(hours, minutes, seconds)
    }
}
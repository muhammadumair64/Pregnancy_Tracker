package com.example.pregnancytrackerignite.data.converters

import androidx.room.TypeConverter
import com.example.pregnancytrackerignite.data.models.GestationalAge


class GestationalAgeConverter {
    @TypeConverter
    fun fromGestationalAge(value: GestationalAge?): String? {
        return value?.let { "${it.weeks},${it.days}" }
    }

    @TypeConverter
    fun toGestationalAge(value: String?): GestationalAge? {
        return value?.split(",")?.let { GestationalAge(it[0].toInt(), it[1].toInt()) }
    }
}
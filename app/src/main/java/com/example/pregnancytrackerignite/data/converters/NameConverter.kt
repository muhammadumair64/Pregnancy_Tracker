package com.iobits.tech.pregnancytracker.data.converters

import androidx.room.TypeConverter

class NameConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
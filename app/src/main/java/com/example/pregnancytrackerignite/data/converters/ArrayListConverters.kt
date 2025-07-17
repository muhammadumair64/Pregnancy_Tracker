package com.example.pregnancytrackerignite.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ArrayListConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromIntArrayList(value: ArrayList<Int>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntArrayList(value: String): ArrayList<Int> {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(value, type)
    }
}

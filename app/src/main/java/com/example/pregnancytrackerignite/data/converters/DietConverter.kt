package com.example.pregnancytrackerignite.data.converters

import androidx.room.TypeConverter
import com.example.pregnancytrackerignite.data.models.DietItem
import com.google.errorprone.annotations.Keep
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Keep
class DietConverter {
    @TypeConverter
    fun fromDietItemList(value: List<DietItem>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDietItemList(value: String?): List<DietItem>? {
        val gson = Gson()
        val listType = object : TypeToken<List<DietItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}
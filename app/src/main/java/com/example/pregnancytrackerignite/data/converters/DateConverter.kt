package com.example.pregnancytrackerignite.data.converters

import androidx.room.TypeConverter
import com.example.pregnancytrackerignite.data.models.SymptomItem
import com.example.pregnancytrackerignite.data.models.Time
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTime(time: Time): String {
        return "${time.hour}:${time.minute}"
    }

    @TypeConverter
    fun toTime(timeString: String): Time {
        val parts = timeString.split(":")
        return Time(parts[1].toInt(), parts[0].toInt())
    }
    @TypeConverter
    fun fromSymptomItemList(symptomItems: List<SymptomItem>?): String {
        return Gson().toJson(symptomItems)
    }

    @TypeConverter
    fun toSymptomItemList(symptomItemsString: String): List<SymptomItem>? {
        val listType = object : TypeToken<List<SymptomItem>>() {}.type
        return Gson().fromJson(symptomItemsString, listType)
    }

}
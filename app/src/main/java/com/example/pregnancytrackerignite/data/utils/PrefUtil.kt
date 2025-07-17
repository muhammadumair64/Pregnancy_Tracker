@file:Suppress("UNCHECKED_CAST")

package com.example.pregnancytrackerignite.data.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.google.gson.Gson

object SavePref {
    val sharedPref by lazy {
        MyApplication.mInstance?.getSharedPreferences("myprefs", MODE_PRIVATE)
    }
    val editor: SharedPreferences.Editor? = sharedPref?.edit()
    val gson by lazy {
        Gson()
    }

    fun <T> setPrefData(key: String, value: T) {
        when (value) {
            is String -> editor?.putString(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
            is Float -> editor?.putFloat(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            else -> {
                val jsonValue = gson.toJson(value)
                editor?.putString(key, jsonValue)
            }
        }
        editor?.apply()
    }

    inline fun <reified T> getPrefData(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> sharedPref?.getString(key, defaultValue) as T
            is Int -> sharedPref?.getInt(key, defaultValue) as T
            is Long -> sharedPref?.getLong(key, defaultValue) as T
            is Float -> sharedPref?.getFloat(key, defaultValue) as T
            is Boolean -> sharedPref?.getBoolean(key, defaultValue) as T
            else -> {
                val jsonString = sharedPref?.getString(key, null)
                jsonString?.let {
                    gson.fromJson(jsonString, T::class.java)
                } ?: defaultValue
            }
        }
    }

}
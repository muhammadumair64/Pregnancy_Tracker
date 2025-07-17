package com.example.pregnancytrackerignite.data.models

import com.squareup.moshi.JsonClass

data class MainBlogModel(
    val Author: String,
    val Date: String,
    val Source: String,
    val Title: String,
    val Type: String,
    val id: String,
    val image: String,
    val para: String
)
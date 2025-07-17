package com.example.pregnancytrackerignite.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class MainBlogsClass(
    @Json(name = "Blogs") val blogs: List<MainBlogModel>? = null
)
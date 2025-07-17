package com.example.pregnancytrackerignite.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class BabiesNameClass(
    @Json(name = "Babies") val names: List<BabiesNameModel>? = null
)
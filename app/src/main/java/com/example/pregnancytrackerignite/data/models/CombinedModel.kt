package com.example.pregnancytrackerignite.data.models

data class CombinedData(
    val user: CurrentUser,
    val data: PregnancyPeriod,
    val daysRemaining: String,
    val weight: String,
    val heightRecord: String,
    val trismsterPeriod: Int?
)
package com.example.pregnancytrackerignite.data.utils

import kotlinx.coroutines.flow.update

object Constants {
    const val ITEM_SKU_REMOVE_ADS_ONLY = "remove_ads" // ONE TIME
    var ITEM_SKU_PRO_USER_SUB = "pro_version" // SUBSCRIPTION
     fun getCurrentTrimester(week: Int):Int {
         val trimster:Int = when (week) {
            in 1..12 -> {
                1
            }

            in 13..26 -> {
                2
            }

            in 27..40 -> {
                3
            }

            else -> {
                0
            }
        }
        return trimster
    }
}
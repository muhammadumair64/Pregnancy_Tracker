package com.example.pregnancytrackerignite.data.utils

import android.content.Context
import com.example.pregnancytrackerignite.R

object ScansHelper {
     var scanList : ArrayList<Int> =  ArrayList()

    suspend  fun addScansInList(context: Context) {
        scanList.apply {
            for (i in 1..40) {
                val resourceId = context.resources.getIdentifier("scan$i", "drawable", context.packageName)
                add(resourceId)
            }
        }
    }
}
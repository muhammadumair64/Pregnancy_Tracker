package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportmodel")
data class ReportDataClass (@PrimaryKey(autoGenerate = true) val id: Long = 0,var filePath: String = "", var title: String = "", var date: String = "", var notes: String = "" , var week: String)
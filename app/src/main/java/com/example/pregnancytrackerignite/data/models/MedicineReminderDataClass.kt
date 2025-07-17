package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "medicineReminder")
data class MedicineReminderDataClass(@PrimaryKey(autoGenerate = true) val id: Long = 0, var days : ArrayList<Int>, var name: String , var medicineTimes : ArrayList<String> , var reminderIds :ArrayList<String>)
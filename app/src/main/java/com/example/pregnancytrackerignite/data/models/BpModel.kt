package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bpmodel")
data class BpModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val date: Date,
    val time: Long,
    val status: Status,
    val notes: Notes?= Notes.Default

)

enum class Notes {
    Default, AfterMeal, BeforeMeal, AfterMedication, Sitting, Lying, Period, AfterWalking
}
enum class Status {
    Normal, Hypertension,ElevatedBP,HighBpStage1,HighBpStage2
}

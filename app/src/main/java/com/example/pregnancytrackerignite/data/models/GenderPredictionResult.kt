package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenderPredictionResult (var gender: String, var solvedQuestion: Int, var totalQuestions: Int,var boyPoints : Int,var girlPoints : Int , @PrimaryKey(autoGenerate = false) val id:Int=0 ,)
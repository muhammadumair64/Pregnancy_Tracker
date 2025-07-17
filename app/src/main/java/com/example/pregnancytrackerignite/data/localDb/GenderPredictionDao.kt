package com.example.pregnancytrackerignite.data.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.models.GenderPredictionResult
import com.example.pregnancytrackerignite.presentation.fragments.GenderPrediction

@Dao
interface GenderPredictionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenderData( predictionResult: GenderPredictionResult)

    @Update
    suspend fun updateGenderData(predictionResult: GenderPredictionResult)

    @Query("Select * from GenderPredictionResult order by id desc limit 1")
    fun getGenderData(): kotlinx.coroutines.flow.Flow<GenderPredictionResult?>
}
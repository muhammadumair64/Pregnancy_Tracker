package com.example.pregnancytrackerignite.data.repositories

import com.example.pregnancytrackerignite.data.localDb.GenderPredictionDao
import com.example.pregnancytrackerignite.data.localDb.UserDao
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.models.GenderPredictionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenderRepository @Inject constructor(private val dao: GenderPredictionDao) {

suspend fun insertGenderPrediction(predictionResult : GenderPredictionResult) {
    withContext(Dispatchers.IO){
        dao.insertGenderData(predictionResult)
    }
}
    suspend fun updateGenderPrediction(predictionResult : GenderPredictionResult){
        withContext(Dispatchers.IO){
            dao.updateGenderData(predictionResult)
        }
    }

    fun getPredictionResult(): Flow<GenderPredictionResult?> {
        return dao.getGenderData()
    }
}
package com.example.pregnancytrackerignite.data.repositories

import com.example.pregnancytrackerignite.data.localDb.PregnancyPeriodDao
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PregnancyRepository @Inject constructor(private val dao: PregnancyPeriodDao) {

    suspend fun insertOrUpdatePregnancyData(user: PregnancyPeriod) {
        withContext(Dispatchers.IO) {
            dao.insertPregnancyData(user)
        }
    }

    suspend fun updateUser(user: PregnancyPeriod) {
        withContext(Dispatchers.IO) {
            dao.updatePregnancyData(user)
        }
    }

    suspend fun deleteUser(user: PregnancyPeriod) {
        withContext(Dispatchers.IO) {
            dao.deletePregnancyData(user)
        }
    }

    fun getCurrentUser(): Flow<PregnancyPeriod?> {
        return dao.getCurrentUser()
    }
}

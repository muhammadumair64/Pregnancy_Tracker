package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.models.BpModel
import javax.inject.Inject

class BloodPressureRepository @Inject constructor(private val dao: BpDao) {
    var latestValues = getLatestBpAdded()
    suspend fun insertBp(bpModel: BpModel) {
        dao.addBpModel(bpModel)
    }

    suspend fun deleteBp(bpModel: BpModel) {
        dao.removeBpModel(bpModel)
    }

    fun getLatestBpAdded(): LiveData<BpModel> {
        return dao.getLatestBpModel()
    }

    fun getAllBpModels(): LiveData<List<BpModel>> {
        return dao.getAllBpModels()
    }
}
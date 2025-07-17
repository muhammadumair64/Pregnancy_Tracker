package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.localDb.ScanDao
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ScanDataClass
import javax.inject.Inject

class ScansRepo @Inject constructor(private val dao: ScanDao) {
    var latestValues = getLatestScanAdded()
    suspend fun insertScan(scan: ScanDataClass) {
        dao.addScanModel(scan)
    }

    suspend fun deleteScan(scan: ScanDataClass) {
        dao.removeScanModel(scan)
    }

    private fun getLatestScanAdded(): LiveData<ScanDataClass> {
        return dao.getLatestScanModel()
    }

    fun getAllScanModels(): LiveData<List<ScanDataClass>> {
        return dao.getAllScanModels()
    }
}
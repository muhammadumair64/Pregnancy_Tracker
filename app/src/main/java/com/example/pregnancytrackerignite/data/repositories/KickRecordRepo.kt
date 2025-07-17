package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.localDb.KickRecordsDao
import com.example.pregnancytrackerignite.data.localDb.ReportDao
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import javax.inject.Inject

class KickRecordRepo @Inject constructor(private val dao: KickRecordsDao) {
    var latestValues = getLatestRecordAdded()
    suspend fun insertKick(record: KickDataClass) {
        dao.addKickModel(record)
    }

    suspend fun deleteKick(report: KickDataClass) {
        dao.removeKickModel(report)
    }

    private fun getLatestRecordAdded(): LiveData<KickDataClass> {
        return dao.getLatestKickModel()
    }

    fun getAllKickModels(): LiveData<List<KickDataClass>> {
        return dao.getAllKickModels()
    }
}
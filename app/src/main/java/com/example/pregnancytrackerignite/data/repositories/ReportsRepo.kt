package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.localDb.ReportDao
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import javax.inject.Inject

class ReportsRepo @Inject constructor(private val dao: ReportDao) {
    var latestValues = getLatestReportAdded()
    suspend fun insertReport(report: ReportDataClass) {
        dao.addReportModel(report)
    }

    suspend fun deleteReport(report: ReportDataClass) {
        dao.removeReportModel(report)
    }

    private fun getLatestReportAdded(): LiveData<ReportDataClass> {
        return dao.getLatestReportModel()
    }

    fun getAllReportModels(): LiveData<List<ReportDataClass>> {
        return dao.getAllReportModels()
    }
}
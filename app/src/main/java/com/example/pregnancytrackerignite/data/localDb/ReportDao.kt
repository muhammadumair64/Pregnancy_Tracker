package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ReportDataClass

@Dao
interface ReportDao {
@Upsert
suspend fun addReportModel(model: ReportDataClass)
@Delete
suspend fun removeReportModel(model: ReportDataClass)
@Query("Select * from reportmodel order by id desc limit 1")
fun getLatestReportModel():LiveData<ReportDataClass>
@Query("Select * from reportmodel order by id desc")
fun getAllReportModels():LiveData<List<ReportDataClass>>
}
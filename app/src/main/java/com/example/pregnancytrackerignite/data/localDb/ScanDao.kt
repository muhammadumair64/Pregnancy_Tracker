package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ScanDataClass

@Dao
interface ScanDao {
@Upsert
suspend fun addScanModel(model: ScanDataClass)
@Delete
suspend fun removeScanModel(model: ScanDataClass)
@Query("Select * from scanmodel order by id desc limit 1")
fun getLatestScanModel():LiveData<ScanDataClass>
@Query("Select * from scanmodel order by id desc")
fun getAllScanModels():LiveData<List<ScanDataClass>>
}
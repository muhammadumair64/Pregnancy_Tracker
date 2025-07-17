package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass

@Dao
interface KickRecordsDao {
@Upsert
suspend fun addKickModel(model: KickDataClass)
@Delete
suspend fun removeKickModel(model: KickDataClass)
@Query("Select * from kickmodel order by id desc limit 1")
fun getLatestKickModel():LiveData<KickDataClass>
@Query("Select * from kickmodel order by id desc")
fun getAllKickModels():LiveData<List<KickDataClass>>
}
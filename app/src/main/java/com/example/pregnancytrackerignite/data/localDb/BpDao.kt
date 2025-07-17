package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pregnancytrackerignite.data.models.BpModel

@Dao
interface BpDao {
@Insert
suspend fun addBpModel(model: BpModel)
@Delete
suspend fun removeBpModel(model: BpModel)
@Query("Select * from BpModel order by id desc limit 1")
fun getLatestBpModel():LiveData<BpModel>
@Query("Select * from BpModel order by id desc")
fun getAllBpModels():LiveData<List<BpModel>>
}
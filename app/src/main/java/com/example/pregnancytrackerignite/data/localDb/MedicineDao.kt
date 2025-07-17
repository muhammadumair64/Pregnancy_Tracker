package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass

@Dao
interface MedicineDao {
@Upsert
suspend fun addMedicineModel(model: MedicineReminderDataClass)
@Delete
suspend fun removeMedicineModel(model: MedicineReminderDataClass)
@Query("Select * from medicineReminder order by id desc limit 1")
fun getLatestMedicineModel():LiveData<MedicineReminderDataClass>
@Query("Select * from medicineReminder order by id desc")
fun getAllMedicineModels():LiveData<List<MedicineReminderDataClass>>
}
package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.localDb.KickRecordsDao
import com.example.pregnancytrackerignite.data.localDb.MedicineDao
import com.example.pregnancytrackerignite.data.localDb.ReportDao
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import javax.inject.Inject

class MedicineReminderRepo @Inject constructor(private val dao: MedicineDao) {

    suspend fun insertReminder(reminder: MedicineReminderDataClass) {
        dao.addMedicineModel(reminder)
    }

    suspend fun deleteReminder(reminder: MedicineReminderDataClass) {
        dao.removeMedicineModel(reminder)
    }

    private fun getLatestReminderAdded(): LiveData<MedicineReminderDataClass> {
        return dao.getLatestMedicineModel()
    }

    fun getAllReminderModels(): LiveData<List<MedicineReminderDataClass>> {
        return dao.getAllMedicineModels()
    }
}
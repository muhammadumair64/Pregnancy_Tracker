package com.example.pregnancytrackerignite.data.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod

@Dao
interface PregnancyPeriodDao {
    @Upsert
    suspend fun insertPregnancyData(user: PregnancyPeriod)

    @Update
    suspend fun updatePregnancyData(user: PregnancyPeriod)

    @Query("Select * from pregnancyperiod order by id desc limit 1")
    fun getCurrentUser(): kotlinx.coroutines.flow.Flow<PregnancyPeriod?>

    @Delete
    suspend fun deletePregnancyData(user: PregnancyPeriod)
}
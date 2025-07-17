package com.example.pregnancytrackerignite.data.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pregnancytrackerignite.data.converters.ArrayListConverters
import com.example.pregnancytrackerignite.data.converters.DateConverter
import com.example.pregnancytrackerignite.data.converters.DietConverter
import com.example.pregnancytrackerignite.data.converters.DurationModelConverter
import com.example.pregnancytrackerignite.data.converters.GestationalAgeConverter
import com.example.pregnancytrackerignite.data.models.BabiesNameModel
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.models.GenderPredictionResult
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.models.ScanDataClass
import com.iobits.tech.pregnancytracker.data.converters.NameConverter

@Database(
    entities = [
        CurrentUser::class,
        BpModel::class,
        PregnancyPeriod::class,
        GenderPredictionResult::class,
        ReportDataClass::class,
        KickDataClass::class,
        MedicineReminderDataClass::class,
        BabiesNameModel::class,
    ScanDataClass::class
               ],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    ArrayListConverters::class,
    DateConverter::class,
    GestationalAgeConverter::class,
    DurationModelConverter::class,
    NameConverter::class,
    DietConverter::class
)

abstract class MainDB : RoomDatabase() {

    abstract val getUserDao: UserDao
    abstract val getPregnancyDao: PregnancyPeriodDao
    abstract fun getBpDao(): BpDao
    abstract fun getReportDao(): ReportDao
    abstract fun getScanDao(): ScanDao
    abstract fun getKickDao(): KickRecordsDao
    abstract fun getMedicineDao(): MedicineDao
    abstract fun getBabiesNameDao(): BabiesNameDao
    abstract val predictionResult :GenderPredictionDao
}
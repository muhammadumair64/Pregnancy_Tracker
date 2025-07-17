package com.example.pregnancytrackerignite.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pregnancytrackerignite.data.localDb.BabiesNameDao
import com.example.pregnancytrackerignite.data.localDb.BpDao
import com.example.pregnancytrackerignite.data.localDb.GenderPredictionDao
import com.example.pregnancytrackerignite.data.localDb.KickRecordsDao
import com.example.pregnancytrackerignite.data.localDb.MainDB
import com.example.pregnancytrackerignite.data.localDb.MedicineDao
import com.example.pregnancytrackerignite.data.localDb.PregnancyPeriodDao
import com.example.pregnancytrackerignite.data.localDb.ReportDao
import com.example.pregnancytrackerignite.data.localDb.ScanDao
import com.example.pregnancytrackerignite.data.localDb.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MyModule {

    var context: Context? = null
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        context = appContext.applicationContext

        return context!!
    }
    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): MainDB {
        return Room.databaseBuilder(context, MainDB::class.java, "pregnancy_bd")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun userDaos(mydatabase: MainDB): UserDao {
        return mydatabase.getUserDao
    }

    @Provides
    @Singleton
    fun pregnancyDaos(mydatabase: MainDB): PregnancyPeriodDao {
        return mydatabase.getPregnancyDao
    }


    @Provides
    @Singleton
    fun genderDao(mydatabase: MainDB): GenderPredictionDao {
        return mydatabase.predictionResult
    }

    @Provides
    @Singleton
    fun bpDao(mydatabase: MainDB): BpDao {
        return mydatabase.getBpDao()
    }

    @Provides
    @Singleton
    fun reportDao(mydatabase: MainDB): ReportDao {
        return mydatabase.getReportDao()
    }

    @Provides
    @Singleton
    fun scanDao(mydatabase: MainDB): ScanDao {
        return mydatabase.getScanDao()
    }

    @Provides
    @Singleton
    fun kickDao(mydatabase: MainDB): KickRecordsDao {
        return mydatabase.getKickDao()
    }

    @Provides
    @Singleton
    fun medicineDao(mydatabase: MainDB): MedicineDao {
        return mydatabase.getMedicineDao()
    }

    @Provides
    @Singleton
    fun babiesNameDao(mydatabase: MainDB): BabiesNameDao {
        return mydatabase.getBabiesNameDao()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Drop existing tables if they exist
            db.execSQL("DROP TABLE IF EXISTS user_diet")
            db.execSQL("DROP TABLE IF EXISTS diet_records")

            // Create the new user_diet table with the updated schema
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS user_diet (
                date INTEGER PRIMARY KEY NOT NULL,
                totalCaloriesNeeded LONG NOT NULL,
                gainedCalories INTEGER NOT NULL,
                breakfastCalories INTEGER NOT NULL DEFAULT 0,
                lunchCalories INTEGER NOT NULL DEFAULT 0,
                dinnerCalories INTEGER NOT NULL DEFAULT 0,
                totalBreakfastCalories INTEGER NOT NULL DEFAULT 0,
                totalLunchCalories INTEGER NOT NULL DEFAULT 0,
                totalDinnerCalories INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
            )

            // Create the new diet_records table with the updated schema
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS diet_records (
                breakfastItemsList TEXT,
                lunchItemsList TEXT,
                dinnerItemsList TEXT,
                date INTEGER PRIMARY KEY NOT NULL
            )
        """.trimIndent()
            )
        }
    }
}

package com.example.pregnancytrackerignite.domain.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.pregnancytrackerignite.data.models.Time
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdatingWorker @Inject constructor(private val context: Context) {
    private val workManager by lazy {
        WorkManager.getInstance(context)
    }

    // Function to start a weekly repeating work request for a custom day and time
    fun startWeeklyReminder(
        id: String, dayOfWeek: Int, appointmentTime: Time, onPreviousDateOrError: () -> Unit
    ) {
        if (dayOfWeek !in Calendar.SUNDAY..Calendar.SATURDAY) {
            throw IllegalArgumentException("Invalid day of the week")
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, appointmentTime.hour)
            set(Calendar.MINUTE, appointmentTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Adjust to the next week if the time has already passed
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val delayInMillis = calendar.timeInMillis - System.currentTimeMillis()

        // Create an initial OneTimeWorkRequest to trigger the first execution
        val initialWorkRequest = OneTimeWorkRequest.Builder(DailyWeatherWorker::class.java)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .build()

        // Enqueue the initial work
        workManager.enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, initialWorkRequest)

        // Create a PeriodicWorkRequest for weekly tasks
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DailyWeatherWorker::class.java, 7, TimeUnit.DAYS)
            .build()

        // Chain the periodic work after the initial work
        workManager.enqueueUniquePeriodicWork(id, ExistingPeriodicWorkPolicy.UPDATE, periodicWorkRequest)
    }


    // Function to cancel a repeating work request
    fun cancelOneTimeWork(id: String) {
        workManager.cancelUniqueWork(id)
    }
}

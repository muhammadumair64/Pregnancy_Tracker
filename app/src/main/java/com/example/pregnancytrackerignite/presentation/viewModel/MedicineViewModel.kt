package com.example.pregnancytrackerignite.presentation.viewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.repositories.MedicineReminderRepo
import com.example.pregnancytrackerignite.data.utils.AlarmReceiver
import com.example.pregnancytrackerignite.data.utils.K
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.example.pregnancytrackerignite.domain.workers.UpdatingWorker
import com.iobits.videocompressor.utils.getRandomNumber
import com.iobits.videocompressor.utils.toTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val reminderRepo: MedicineReminderRepo,
    private val notificationScheduler: UpdatingWorker
) : ViewModel() {

    var allReminderLiveData = reminderRepo.getAllReminderModels()

    fun deleteReportModel(
        context: Context,
        reminderModel: MedicineReminderDataClass,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            reminderModel.reminderIds.forEach {
            //    notificationScheduler.cancelOneTimeWork(it)
                cancelAlarm(context,it.toInt(),reminderModel.name,reminderModel.days)
            }
            reminderRepo.deleteReminder(reminderModel)
            onDone.invoke()
        }
    }

    fun upsertReminder(
        context: Context,
        id : Long,
        days: ArrayList<Int>,
        name: Editable,
        medicineTimes: ArrayList<String>,
        onDone: ()->Unit
    ) {
        viewModelScope.launch {
            val reminderIds = ArrayList<String>()
            medicineTimes.forEach { time->
                days.forEach { day ->
                    var id = MyApplication.mInstance.preferenceManager.getInt(
                        PreferencesManager.Key.REMINDER_ID)
                    id++
                    MyApplication.mInstance.preferenceManager.put(PreferencesManager.Key.REMINDER_ID,id)
                    reminderIds.add(id.toString())
                    Log.d("setAlarm", "upsertReminder: $time")
                        //notificationScheduler.startWeeklyReminder(id.toString(),day,it){}
                        setAlarm(context,name.toString(),getMillisFromTimeString(time),id.toInt(), listOf(day))
                }
            }

            reminderRepo.insertReminder(MedicineReminderDataClass(id,days,
                name.toString(),medicineTimes,reminderIds))
            onDone.invoke()
        }
    }

    fun getMillisFromTimeString(timeString: String): Long {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Use "hh:mm a" for 12-hour format with AM/PM
        return try {
            val date = timeFormat.parse(timeString)
            date?.time ?: 0L // Return milliseconds or 0 if parsing fails
        } catch (e: Exception) {
            e.printStackTrace()
            0L // Return 0 in case of an error
        }
    }
    private fun setAlarm(
        context: Context,
        name: String,
        triggerTimeMillis: Long,
        alarmId: Int,
        selectedDays: List<Int>
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        // Ensure selectedDays are sorted in the correct order for scheduling
        val sortedDays = selectedDays.sorted()

        for (day in sortedDays) {
            // Calculate the trigger time for the next occurrence of the day
            val triggerTime = calculateNextTriggerTime(triggerTimeMillis, currentDayOfWeek, day)

            val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(K.REMINDER_TYPE, name)
                putExtra(K.DAY_LIST, selectedDays.toIntArray())
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId + day,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule the alarm
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                    pendingIntent
                )

                if (!alarmManager.canScheduleExactAlarms()) {
                    context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
                Log.d("Alarm", "setAlarm: SET at time ")
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    AlarmManager.INTERVAL_DAY * 7, // Weekly interval
                    pendingIntent
                )
                Log.d("Alarm", "setAlarm: SET at time ")
            }
        }
    }

    private fun calculateNextTriggerTime(
        baseTimeMillis: Long,
        currentDay: Int,
        targetDay: Int
    ): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = baseTimeMillis
        }

        // Calculate the difference in days
        val daysDifference = if (targetDay >= currentDay) {
            targetDay - currentDay
        } else {
            7 - (currentDay - targetDay)
        }

        // Add the days difference to the current time
        calendar.add(Calendar.DAY_OF_YEAR, daysDifference)
        return calendar.timeInMillis
    }


    fun cancelAlarm(context: Context,alarmId: Int, reminderType: String, selectedDays: List<Int>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("REMINDER_TYPE", reminderType)
            val item = selectedDays.toIntArray()
            intent.putExtra("DAY_LIST", item)
            PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.cancel(alarmIntent) }

    private fun cancelAlarmAbove12(context: Context,name: String , alarmId: Long, selectedDays: List<Int>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(K.REMINDER_TYPE, name)
            putExtra(K.DAY_LIST, selectedDays.toIntArray())
        }

        // Get the current day of the week and calculate its index (0 for Sunday, 1 for Monday, ..., 6 for Saturday)
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val currentDayIndex = (currentDayOfWeek + 5) % 7 // Adjusted index for the array

        // Arrange the selected days for next week starting from the current day
        val arrangedDays = mutableListOf<Int>()
        for (i in currentDayIndex until 7) {
            if (selectedDays.contains(i)) {
                arrangedDays.add(i)
            }
        }
        for (i in 0 until currentDayIndex) {
            if (selectedDays.contains(i)) {
                arrangedDays.add(i)
            }
        }

        // Cancel the alarm for each arranged day
        for (dayIndex in arrangedDays) {
            val requestCode = alarmId + dayIndex + 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Cancel the alarm using the PendingIntent and the specified ID
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    //-------------------------------- Get Time Utils -----------------------------//
    private fun calculateTriggerTime(currentTriggerTime: Long, currentDayIndex: Int, targetDayIndex: Int): Long {
        val millisecondsInADay = 24 * 60 * 60 * 1000L
        val daysToAdd = (targetDayIndex + 7 - currentDayIndex) % 7 // Calculate days to add for the target day
        return currentTriggerTime + daysToAdd * millisecondsInADay // Calculate trigger time for the target day
    }

    private fun getMillis(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

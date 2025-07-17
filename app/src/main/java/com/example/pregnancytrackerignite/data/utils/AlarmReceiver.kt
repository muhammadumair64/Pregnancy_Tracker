package com.example.pregnancytrackerignite.data.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.example.pregnancytrackerignite.presentation.activities.MainActivity
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    private var dayArray: IntArray? = null
     var type = ""
     var TAG = "AlarmReceiverTag"
    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context, intent: Intent) {
        if(intent != null){
            Log.d("ReminderActivityTag", "onReceive: Intent Received")
            try{
                type = intent.getStringExtra(K.REMINDER_TYPE).toString()
                dayArray = intent.getIntArrayExtra(K.DAY_LIST)
            }catch (e:Exception){
                e.localizedMessage
            }
        }
        val content = "Don't forget to take $type. Stay on track for a healthy routine!"
        val reminder = "Time to Take Your Medicine!"
         val calendar = Calendar.getInstance()
         val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
         val day = dayOfWeek - 1
        if(dayArray?.contains(day)== true){
            Log.d("AlarmReceiver", "onReceive: ${intent.data}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "channel_id",
                    "Medicine Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = content
                    enableVibration(true)
                    canShowBadge()
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null) // Set notification sound
                    // Add other channel settings as needed
                }
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            Log.d("AlarmReceiver", "onReceive: ${intent.data}")
            // Create an Intent to launch your app when the notification is clicked
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) // Use the default notification sound

            // Create the notification
            val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(reminder)
                .setContentText(content).setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setVibrate(longArrayOf(0, 1000, 1000, 1000)) // Vibrate pattern (optional)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Show the notification
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(324, notificationBuilder.build())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                try {
                    val nextWeekCalendar = Calendar.getInstance()
                    nextWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1)
                    val nextWeekTimeInMillis = nextWeekCalendar.timeInMillis
                    val id = MyApplication.mInstance.preferenceManager.getInt(PreferencesManager.Key.REMINDER_ID, 401)
                    val newID = id + 8
                    setAlarm(context,nextWeekTimeInMillis,newID,dayArray)
                }catch (e:Exception){
                    e.localizedMessage
                }
              }
           }
        }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarm(context: Context, triggerTimeMillis: Long, alarmId: Int, selectedDays: IntArray?) {
            Log.d(TAG, "setAlarm: Init $triggerTimeMillis ")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra(K.REMINDER_TYPE,type)
                val item = selectedDays
                intent.putExtra(K.DAY_LIST , item)
                PendingIntent.getBroadcast(context, alarmId.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
            }

            // Check if the app has the SCHEDULE_EXACT_ALARM permission
            if (alarmManager.canScheduleExactAlarms()) {
                // Set the repeating alarm with the specified trigger time in milliseconds
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(triggerTimeMillis, alarmIntent),
                    alarmIntent
                )
                Log.d(TAG, "in if of scheduler ")
            }
    }

}
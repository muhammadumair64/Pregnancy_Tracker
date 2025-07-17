package com.example.pregnancytrackerignite.presentation.fragments

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.utils.ReminderReceiver
import com.example.pregnancytrackerignite.databinding.FragmentLungsExerciseBinding
import com.example.pregnancytrackerignite.presentation.fragments.bottomSheet.CompleteLungsExerciseBottomSheet
import com.example.pregnancytrackerignite.presentation.fragments.bottomSheet.SelectBreathingTimeBottomSheet
import com.iobits.videocompressor.utils.popBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LungsExerciseFragment : Fragment() {

    val binding by lazy {
        FragmentLungsExerciseBinding.inflate(layoutInflater)
    }
    private var isExercising = false
    private var inhaleTime = 4000L // Inhale for 3 seconds
    private var holdTime = 5000L // Hold for 4 seconds
    private var exhaleTime = 4000L // Exhale for 3 seconds
    private val REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            initListeners()
        }catch (e:Exception){
            e.localizedMessage
        }
        return binding.root
    }
    private fun initListeners() {

        binding.backBtn.setOnClickListener {
           popBackStack()
        }

        binding.btnStart.setOnClickListener {
            selectBreathing()
        }

    }

    private fun selectBreathing() {
        val bottomSheetDialog = SelectBreathingTimeBottomSheet { timeInMinutes ->
            lifecycleScope.launch {
                if (!isExercising) {
//                        setInhalHoldTime(timeInMinutes)
                    timeInMinutes?.let { it1 -> startBreathingExercise(it1) }
                }
            }
        }
        requireActivity().supportFragmentManager.let {
            bottomSheetDialog.show(
                it, bottomSheetDialog.tag
            )
        }
    }

    private fun startBreathingExercise(durationInMinutes: Int) {
        isExercising = true
        binding.btnStart.visibility = View.INVISIBLE
        val totalDuration = durationInMinutes * 60 * 1000 // Total duration in milliseconds

        // Start the stopwatch in a separate coroutine
        val timerJob = CoroutineScope(Dispatchers.Main).launch {
            var elapsedTime = 0L
            while (elapsedTime < totalDuration) {
                val minutes = (elapsedTime / 60000) % 60
                val seconds = (elapsedTime / 1000) % 60
                binding.timerTxt.text = String.format("%02d:%02d", minutes, seconds)
                delay(1000) // Update every second
                elapsedTime += 1000
            }
        }

        // Breathing exercise logic
       lifecycleScope.launch {
           try {
               var totalElapsed = 0L
               val cycles = (totalDuration / (inhaleTime + holdTime + exhaleTime + holdTime)).toInt()

               for (i in 0 until cycles) {
                   inhale()
                   totalElapsed += inhaleTime
                   holdBreath()
                   totalElapsed += holdTime
                   exhale()
                   totalElapsed += exhaleTime
                   holdBreath()
                   totalElapsed += holdTime
               }

               // Cancel the timer when done
               timerJob.cancel()
               isExercising = false // Reset after the exercise is completed
//            hintOfActionTxt.text = "" // Clear hint text
               binding.timerTxt.text = "00:00" // Reset timer text
               binding.btnStart.visibility = View.VISIBLE

               val bottomSheetDialog = CompleteLungsExerciseBottomSheet({
                   selectBreathing()
               }) {
                   checkAlarmPermission()
               }
               if (requireActivity().supportFragmentManager?.isDestroyed == false) requireActivity().supportFragmentManager?.let { bottomSheetDialog.show(it, bottomSheetDialog.tag) }
           } catch (e:Exception){
               e.localizedMessage
           }
        }
    }

    private suspend fun inhale() {
        animateCircleSize(150f) // Increase size for inhale
        binding.hintOfActionTxt.text = "INHALE"
        delay(inhaleTime) // Inhale for 3 seconds
    }

    private suspend fun holdBreath() {
        // Hold breath (you can add any UI feedback here)
        binding.hintOfActionTxt.text = "HOLD"
        delay(holdTime) // Hold for 4 seconds
    }

    private suspend fun exhale() {
        binding.hintOfActionTxt.text = "EXHALE"
        animateCircleSize(100f) // Decrease size to original for exhale
        delay(exhaleTime)
    }

    private fun animateCircleSize(size: Float) {
        binding.whiteCircle.animate()
            .scaleX(size / 100) // Scale based on the original size
            .scaleY(size / 100
            ).setDuration(inhaleTime) // Duration of the scaling animation
            .start()
    }


    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireActivity().getSystemService(AlarmManager::class.java)?.canScheduleExactAlarms() == false) {
                // Launch intent to request permission
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM)
            } else {
                setReminderAfter24Hours()
            }
        } else {
            setReminderAfter24Hours()

        }
    }

    private fun setReminderAfter24Hours() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireContext(), ReminderReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val triggerTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)
        // Set exact alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent)
        popBackStack()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if ( requireActivity().getSystemService(AlarmManager::class.java)?.canScheduleExactAlarms() == false) {
                    // Launch intent to request permission
                    Toast.makeText(requireContext(), "Permission not Granted", Toast.LENGTH_SHORT).show()
                   popBackStack()
                } else {
                    setReminderAfter24Hours()
                }
            } else {
                setReminderAfter24Hours()
            }
        }
    }

}
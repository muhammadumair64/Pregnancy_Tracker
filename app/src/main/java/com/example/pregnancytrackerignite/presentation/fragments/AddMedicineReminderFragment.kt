package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentAddMedicineReminderBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.example.pregnancytrackerignite.presentation.viewModel.MedicineViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iobits.videocompressor.utils.closeKeyboard
import com.iobits.videocompressor.utils.disableMultipleClicking
import com.iobits.videocompressor.utils.hideKeyboard
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.singleToast
import com.iobits.videocompressor.utils.toEditable
import com.iobits.videocompressor.utils.visible
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale


class AddMedicineReminderFragment : Fragment() {

    private val binding by lazy { FragmentAddMedicineReminderBinding.inflate(layoutInflater) }
    private val viewModel: MedicineViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var isNotificationOn = false
    private val selectedDays = ArrayList<Int>()
    private var current = 0
    private val timeArrayList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root.also {
    try {
        initViews()
    }catch (e:Exception){
        e.localizedMessage
      }
    }

    private fun initViews() {

        if(sharedViewModel.isFromReminderRV){
            dataSetter()
        }

        try {
            setupDaySelection()
            setupTimePickers()
            setupButtons()
        }catch (e:Exception){
            e.localizedMessage
        }


        binding.deleteBtn.setOnClickListener {
            viewModel.deleteReportModel(requireContext(),sharedViewModel.selectedReminder!!) {
                requireActivity().singleToast("Delete Reminder Successfully")
                popBackStack()
            }
        }
    }


    private fun setupDaySelection() {
        val dayButtonMap = mapOf(
            binding.sat to Calendar.SATURDAY,
            binding.sun to Calendar.SUNDAY,
            binding.mon to Calendar.MONDAY,
            binding.tue to Calendar.TUESDAY,
            binding.wed to Calendar.WEDNESDAY,
            binding.thr to Calendar.THURSDAY,
            binding.fri to Calendar.FRIDAY
        )

        dayButtonMap.forEach { (button, day) ->
            button.setOnClickListener {
                toggleDaySelection(button, day)
            }
        }
    }

    private fun toggleDaySelection(view: View, day: Int) {
        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
            view.setBackgroundResource(R.drawable.small_stroke_background)
        } else {
            selectedDays.add(day)
            view.setBackgroundResource(R.drawable.small_pink_stroke_background)
        }
    }

    private fun setupTimePickers() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTheme(R.style.CustomTimePickerTheme)
            .build()

        binding.apply {
            selectDate1Lay.setOnClickListener {
                closeKeyboard(requireActivity())
                disableMultipleClicking(it)
                showTimePicker(timePicker, 1) }
            selectDate2Lay.setOnClickListener {
                closeKeyboard(requireActivity())
                disableMultipleClicking(it)
                showTimePicker(timePicker, 2) }
            selectDate3Lay.setOnClickListener {
                closeKeyboard(requireActivity())
                disableMultipleClicking(it)
                showTimePicker(timePicker, 3) }
        }

        timePicker.addOnPositiveButtonClickListener {
            val selectedTime = formatTime(timePicker.hour, timePicker.minute)
            updateTimeText(current, selectedTime)
        }
    }

    private fun showTimePicker(timePicker: MaterialTimePicker, position: Int) {
        current = position
        try {
            timePicker.show(requireActivity().supportFragmentManager, "Time_Picker")
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("TimePicker", it) }
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun updateTimeText(position: Int, time: String) {
        when (position) {
            1 -> {
                binding.dateText.text = time
                updateTimeArrayList(0, time)
            }
            2 -> {
                binding.date2Text.text = time
                updateTimeArrayList(1, time)
            }
            3 -> {
                binding.date3Text.text = time
                updateTimeArrayList(2, time)
            }
        }
    }

    private fun updateTimeArrayList(index: Int, time: String) {
        if (index >= timeArrayList.size) {
            while (timeArrayList.size <= index) timeArrayList.add("")
        }
        timeArrayList[index] = time
    }

    private fun setupButtons() {
        binding.apply {
            startBtn.setOnClickListener {
                    if (selectedDays.isEmpty() || timeArrayList.isEmpty()) {
                        requireActivity().singleToast("Please select days and time carefully")
                    } else {
                        if(sharedViewModel.isFromReminderRV){
                            setMedicineReminders(sharedViewModel.selectedReminder?.id ?:0)
                        }else{
                            setMedicineReminders(0)
                        }
                    }
            }

            notiToggle.setOnClickListener {
                isNotificationOn = !isNotificationOn
                notiToggle.setImageResource(
                    if (isNotificationOn) R.drawable.toggle_on else R.drawable.toggle_off
                )
            }

            backBtn.setOnClickListener { popBackStack() }

            selectorBg.setOnClickListener { openPopupMenu(binding.upArrow) }
        }
    }

    private fun openPopupMenu(anchor: View) {
        closeKeyboard(requireActivity())
        val popupMenu = PopupMenuCustomLayout(
            requireContext(), R.layout.medi_routine_menu
        ) { item ->
            when (item) {
                R.id.one -> updateRoutineSelection("Once a day", 1)
                R.id.two -> updateRoutineSelection("Twice a day", 2)
                R.id.three -> updateRoutineSelection("Three times a day", 3)
            }
        }
        popupMenu.showSettingDialog(anchor)
    }

    private fun updateRoutineSelection(routineText: String, visibleCount: Int) {
        binding.apply {
            selectorText.text = routineText
            selectDate1Lay.visible()
            selectDate2Lay.visibility = if (visibleCount >= 2) View.VISIBLE else View.GONE
            selectDate3Lay.visibility = if (visibleCount == 3) View.VISIBLE else View.GONE
        }
    }

    private fun setMedicineReminders(id:Long) {
        viewModel.upsertReminder(requireContext(),id,selectedDays, binding.nameEt.text, timeArrayList) {
            requireActivity().singleToast("Successfully Added")
            popBackStack()
        }
    }


    private fun dataSetter() {
        sharedViewModel.apply {
         binding.apply {
             if(selectedReminder != null){
                 nameEt.text = selectedReminder?.name?.toEditable()
                 when(selectedReminder?.medicineTimes?.size){
                  1->{
                      updateRoutineSelection("Once a day",1)
                      dateText.text = selectedReminder!!.medicineTimes[0]
                  }
                  2->{
                      updateRoutineSelection("Twice a day", 2)
                      dateText.text = selectedReminder!!.medicineTimes[0]
                      date2Text.text = selectedReminder!!.medicineTimes[1]
                  }
                  3->{
                      updateRoutineSelection("Three times a day", 3)
                      dateText.text = selectedReminder!!.medicineTimes[0]
                      date2Text.text = selectedReminder!!.medicineTimes[1]
                      date3Text.text = selectedReminder!!.medicineTimes[2]
                  }
                }
                 selectedReminder?.days?.forEach {
                     when (it) {
                         Calendar.MONDAY -> toggleDaySelection(binding.mon, it)
                         Calendar.TUESDAY -> toggleDaySelection(binding.tue, it)
                         Calendar.WEDNESDAY -> toggleDaySelection(binding.wed, it)
                         Calendar.THURSDAY -> toggleDaySelection(binding.thr, it)
                         Calendar.FRIDAY -> toggleDaySelection(binding.fri, it)
                         Calendar.SATURDAY -> toggleDaySelection(binding.sat, it)
                         Calendar.SUNDAY -> toggleDaySelection(binding.sun, it)
                         else -> {}
                     }
                 }
                 deleteBtn.visible()
                 timeArrayList.addAll(selectedReminder!!.medicineTimes)
                 selectedDays.addAll(selectedReminder!!.days)
              }
           }
        }
    }
}

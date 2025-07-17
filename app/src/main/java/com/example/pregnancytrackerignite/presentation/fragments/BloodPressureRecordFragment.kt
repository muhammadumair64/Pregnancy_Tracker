package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.Notes
import com.example.pregnancytrackerignite.data.models.Status
import com.example.pregnancytrackerignite.databinding.FragmentBloodPressureRecordBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iobits.videocompressor.utils.formatToCustomString
import com.iobits.videocompressor.utils.getCurrentDate
import com.iobits.videocompressor.utils.getCurrentTime
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BloodPressureRecordFragment : Fragment() {

    private val binding by lazy { 
        FragmentBloodPressureRecordBinding.inflate(layoutInflater)
    }

    private var selectedDate: Date? = null
    private var selectedTime: Long? = null
    private var status = Status.Normal
    private var isDefaultValue = true
    private val viewModel: BloodPressureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        initListeners()
        initViews()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        handleBackPress {
            safeNavigate(R.id.action_bloodPressureRecordFragment_to_mainFragment, R.id.bloodPressureRecordFragment)
        }
    }
    private fun initViews() {

        binding.dateText.text = getCurrentDate()
        binding.timeTextWithAmPm.text = getCurrentTime()

        binding.seekbar.isEnabled = false
        binding.seekbar.setOnTouchListener{ _, _ -> true }  // Returning true prevents all touch events
        binding.seekbar.isFocusable = false
        binding.seekbar.isClickable = false
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            safeNavigate(R.id.action_bloodPressureRecordFragment_to_mainFragment, R.id.bloodPressureRecordFragment)
        }

        binding.numberpickerSystolic.setOnValueChangedListener { picker, oldVal, newVal ->
            val n = newVal
            checkLevel(newVal, binding.numberpickerdiaStolic.value)
            isDefaultValue = false
            Log.d("CHECK_KARIS", "initListeners: $newVal")
        }

        binding.numberpickerdiaStolic.setOnValueChangedListener { picker, oldVal, newVal ->
            checkLevel(binding.numberpickerSystolic.value, newVal)
            isDefaultValue = false
        }

        binding.numberpickerpulse.setOnValueChangedListener{ _ , _ , _ ->
            isDefaultValue = false
        }

        binding.calenderCard.setOnClickListener {
            datePickerDialogue()
        }

        binding.timeCard.setOnClickListener {
            timePickerDialogue()
        }

        binding.saveBtn.setOnClickListener {
            if (isDefaultValue) Toast.makeText(requireContext(), "Enter your Values first...", Toast.LENGTH_SHORT).show()
            else saveDataInDp()
        }
    }

    private fun saveDataInDp() {
        if (binding.numberpickerSystolic.value < binding.numberpickerdiaStolic.value) {
            Toast.makeText(requireContext(), "Systolic cant be less than diastolic", Toast.LENGTH_SHORT).show()
            return
        }
        val model = BpModel(
            0,
            binding.numberpickerSystolic.value,
            binding.numberpickerdiaStolic.value,
            binding.numberpickerpulse.value,
            selectedDate ?: Date(),
            selectedTime ?: System.currentTimeMillis(),
            status,
            Notes.Default
        )
        MyApplication.tempBpModelToSave.value = model
        viewModel.addBpModel(model)
        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
        safeNavigate(R.id.action_bloodPressureRecordFragment_to_bloodPressureResultFragment, R.id.bloodPressureRecordFragment)
//        startActivity(Intent(this, BloodPressureResultActivity::class.java))
//        finish()
    }

    private fun timePickerDialogue() {

        val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(12)
            .setMinute(10).setTitleText("Select time")
            .setTheme(R.style.CustomTimePickerTheme)  // Apply the custom theme here
            .build()
        requireActivity().supportFragmentManager.let { fragmentManager ->
            picker.show(
                fragmentManager, "timepicker"
            )
        }
        picker.addOnPositiveButtonClickListener {
            isDefaultValue = false
            // Update the selected time in the TextView
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, picker.hour)
                set(Calendar.MINUTE, picker.minute)
            }.timeInMillis

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
            val formattedTime: kotlin.String = timeFormat.format(selectedTime)
            binding.timeTextWithAmPm.text = formattedTime
        }
    }

    private fun datePickerDialogue() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select Date")
            .setTheme(R.style.CustomDatePickerTheme) // Apply the custom theme

        val constraintsBuilder = CalendarConstraints.Builder()

        val currentMillis = System.currentTimeMillis()
        constraintsBuilder.setStart(currentMillis - TimeUnit.DAYS.toMillis(365 * 3)) // Example: Set minimum year to 5 years ago
        constraintsBuilder.setEnd(currentMillis + TimeUnit.DAYS.toMillis(365 * 3))   // Example: Set maximum year to 5 years in the future

        builder.setCalendarConstraints(constraintsBuilder.build())
        val materialDatePicker = builder.build()
        materialDatePicker.show(requireActivity().supportFragmentManager, "TagFour")
        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            isDefaultValue = false
            Log.d("DATE_PICKER", "$selection")
            val startDates = Date(selection)
            selectedDate = startDates
            binding.dateText.text = startDates.formatToCustomString()

        }
    }

    private fun checkLevel(systolic: Int, diastolic: Int) {
        resetTextViews()
        if (systolic < 90 && diastolic < 60) {
            // hypotension
            status = Status.Hypertension
            binding.bpStateText.text = getString(R.string.hypertension)
            binding.hypotension.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.hypotensionValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.seekbar.progress = 2
        } else if (systolic in 90..119 && diastolic in 60..79) {
            // normal
            status = Status.Normal
            binding.bpStateText.text = getString(R.string.normal)
            binding.bpNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.bpNormalValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.seekbar.progress = 15
        } else if (systolic in 119..129 && diastolic <= 80) {
            // elevated
            status = Status.ElevatedBP
            binding.bpStateText.text = getString(R.string.elevated)
            binding.elevated.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.elevatedValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.seekbar.progress = 29
        } else if (systolic in 129..139 && diastolic >= 80) {
//            hypertension stage 1
            status = Status.HighBpStage1
            binding.bpStateText.text = getString(R.string.hypertension_stage_1)
            binding.hypertension1.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.hypertension1Value.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.seekbar.progress = 42
        } else if (systolic >= 140 && diastolic >= 90) {
//            hypertension stage 2
            status = Status.HighBpStage2
            binding.bpStateText.text = getString(R.string.hypertension_stage_2)
            binding.hypertension2.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.hypertension2Value.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.seekbar.progress = 55
        }
    }

    private fun resetTextViews() {
        binding.hypotension.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.hypotensionValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.bpNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.bpNormalValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.elevated.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.elevatedValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.hypertension1.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.hypertension1Value.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.hypertension2.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        binding.hypertension2Value.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
    }


}
package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentOvulationBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.isDateWithin9Months
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.videocompressor.utils.hideKeyboard
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import java.util.Date

class OvulationFragment : Fragment() {

    val binding by lazy {
        FragmentOvulationBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    fun initViews() {
        binding.apply {
            backBtn.setOnClickListener {
                popBackStack()
            }

            calenderBg.setOnFocusChangeListener{ _, hasFocus ->
                if (hasFocus) {
                    calenderBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    calenderBg.setBackgroundResource(R.drawable.field_bg)
                }
            }

            calenderBg.setOnClickListener {
                selectDate()
            }

            daysBg.setOnClickListener {
                hideKeyboard(it)
                val popupMenu = PopupMenuCustomLayout(
                    requireContext(), R.layout.popupchooserovulataiondays
                ) { item ->
                    // Check the ID of the clicked item and handle it dynamically
                    val selectedDay = item - R.id.day21 + 21 // Calculate the day from the ID
                    if (selectedDay in 21..35) {
                        daysTv.text = "$selectedDay Days"
                        daysTv.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.black)
                        )
                        viewModel.periodCycleDays = selectedDay.toString()
                        checkConditionsAndUpdateButton()
                    }
                }
                popupMenu.showSettingDialog(upArrow)
            }

            btnShowCalender.setOnClickListener{
                if (binding.calenderTv.text.toString().isEmpty() || binding.daysTv.text.toString().isEmpty()){
                    showToast("Please fill all the fields")
                    return@setOnClickListener
                } else {
                    binding.calenderTv.text = ""
                    binding.daysTv.text = ""
                    safeNavigate(
                        R.id.action_ovulationFragment_to_ovulationCalenderFragment,
                        R.id.ovulationFragment
                    )
                }
            }
        }
    }

    private fun checkConditionsAndUpdateButton() {
        binding.apply {
            if (binding.calenderTv.text.toString().isEmpty() || binding.daysTv.text.toString().isEmpty()) {
                btnShowCalender.alpha = 0.5f // or set the background directly if you have different drawable
                btnShowCalender.isClickable = false
            } else {
                btnShowCalender.alpha = 1f
                btnShowCalender.isClickable = true
            }
        }
    }

    private fun selectDate() {
        val today = CivilCalendar()
        val datePicker = PrimeDatePicker.dialogWith(today).pickSingleDay { day ->
            logd(day.timeInMillis.toString())
            logd(Date(day.timeInMillis).toString())
            if (!day.timeInMillis.isDateWithin9Months()) {
                showToast("Date can't be more than 9 months")
            } else {
                binding.calenderTv.text = day.longDateString
                viewModel.lastPeriodDate = day.longDateString
                checkConditionsAndUpdateButton()
            }
        }.initiallyPickedSingleDay(today).maxPossibleDate(today).build()
        activity?.supportFragmentManager?.let { fragmentManager ->
            datePicker.show(
                fragmentManager, "Date_Picker_CC"
            )
        }
    }
}
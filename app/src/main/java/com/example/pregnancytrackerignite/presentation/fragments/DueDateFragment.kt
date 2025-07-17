package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentDueDateBinding
import com.example.pregnancytrackerignite.presentation.fragments.bottomSheet.GestionalAgeSelector
import com.example.pregnancytrackerignite.presentation.viewModel.PregnancyViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.iobits.rubik_cube_solver.data.utils.isDateWithin9Months
import com.iobits.rubik_cube_solver.data.utils.isExpectedPregnancyDateValid
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.videocompressor.utils.disableMultipleClicking
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DueDateFragment : Fragment() {

    private val binding by lazy {
        FragmentDueDateBinding.inflate(layoutInflater)
    }
    private var selectionForPicker = 0
    private var allowToNavigate = false
    private val viewModel: SharedViewModel by activityViewModels()
    private val detailsViewModel: PregnancyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        return binding.root
    }

    private fun initView() {

        binding.dateBg.setOnClickListener {
            disableMultipleClicking(it)
//                datePickerDialogue()
            openChooser()
        }
        lifecycleScope.launch {
            viewModel.pregnancyData.flowWithLifecycle(lifecycle).collectLatest { data ->
                logd(data.toString())
                data?.let {
                    binding.startBtn.alpha = 1f
                    allowToNavigate = true
                }
            }
        }

        binding.basedOnBg.setOnClickListener {
            val popupMenu = PopupMenuCustomLayout(
                requireContext(), R.layout.basedondue
            ) { item ->
                when (item) {
                    R.id.start_date_of_last_period -> {
                        selectionForPicker = 0
                        binding.selectDateLay.visible()
                        binding.basedOnText.text = "Start date of last period"
                        binding.basedOnText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                    }
                    R.id.gestational_age -> {
                        selectionForPicker = 1
                        binding.selectDateLay.visible()
                        binding.basedOnText.text = "Gestational age"
                        binding.basedOnText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                    }
                    R.id.estimate_of_due_date -> {
                        selectionForPicker = 2
                        binding.selectDateLay.visible()
                        binding.basedOnText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                        binding.basedOnText.text = "Estimate of due date"
                    }
                }
            }
            popupMenu.showSettingDialog(binding.upArrow)
        }

        binding.startBtn.setOnClickListener {
            if (allowToNavigate) safeNavigate(
                R.id.action_dueDateFragment_to_dueDateResultFragment,
                R.id.dueDateFragment
            )
        }
    }

    private fun openChooser() {
        when (selectionForPicker) {
            0 -> {
                selectLastPeriodDate()
            }
            1 -> {
                val sheet = GestionalAgeSelector{ age->
                    logd(age.toString())
                    binding.dateText.text = age.toString()
                    binding.startBtn.alpha = 1f
                    detailsViewModel.addOrUpdatePregnancyData(
                        PregnancyPeriod(
                            null, age, null,
                        )
                    )
                }
                activity?.supportFragmentManager?.let { fragmentManger ->
                    sheet.show(
                        fragmentManger, sheet.tag
                    )
                }
            }
            2 -> {
                selectEstimatedDueDate()
            }
        }
    }

//    private fun selectEstimatedDueDate() {
//        val today = CivilCalendar()
//        val datePicker = PrimeDatePicker.dialogWith(today).pickSingleDay { day ->
//                logd(day.longDateString)
//                logd(day.timeInMillis.toString())
//                if (!day.timeInMillis.isExpectedPregnancyDateValid()) {
//                    showToast("Date can't be more than 9 months")
//                } else {
//                    binding.dateText.text = day.longDateString
//                    detailsViewModel.addOrUpdatePregnancyData(
//                        PregnancyPeriod(
//                            null, null, estimatedDate = (day.timeInMillis),
//                        )
//                    )
//                }
//            }.initiallyPickedSingleDay(today).minPossibleDate(today).build()
//        activity?.supportFragmentManager?.let { fragmentManager ->
//            datePicker.show(
//                fragmentManager, "Date_Picker_CC"
//            )
//        }
//    }
//
//    private fun selectLastPeriodDate() {
//        val today = CivilCalendar()
//        val datePicker = PrimeDatePicker
//            .dialogWith(today).pickSingleDay { day ->
//                logd(day.timeInMillis.toString())
//                logd(Date(day.timeInMillis).toString())
//                if (!day.timeInMillis.isDateWithin9Months()) {
//                    showToast("Date can't be more than 9 months")
//                } else {
//                    binding.dateText.text = day.longDateString
//                    detailsViewModel.addOrUpdatePregnancyData(
//                        PregnancyPeriod(
//                            (day.timeInMillis), null, null
//                        )
//                    )
//                }
//            }.initiallyPickedSingleDay(today).maxPossibleDate(today)
//            .build()
//        activity?.supportFragmentManager?.let { fragmentManager ->
//            datePicker?.show(
//                fragmentManager, "Date_Picker_CC"
//            )
//        }
//    }

    private fun selectEstimatedDueDate() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        // Define a CalendarConstraint to disable past dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(today) // Restrict selectable dates to today and after
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Estimated Due Date")
            .setTheme(R.style.CustomDatePickerTheme)
            .setSelection(today) // Set default selection to today
            .setCalendarConstraints(constraintsBuilder) // Apply constraints
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            logd(Date(selectedDateInMillis).toString())
            if (!selectedDateInMillis.isExpectedPregnancyDateValid()) {
                showToast("Date can't be more than 9 months")
            } else {
                binding.dateText.text = datePicker.headerText
                detailsViewModel.addOrUpdatePregnancyData(
                    PregnancyPeriod(
                        null, null, estimatedDate = selectedDateInMillis
                    )
                )
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "Estimated_Due_Date_Picker")
    }


    private fun selectLastPeriodDate() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        // Define a CalendarConstraint to disable future dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today) // Restrict selectable dates to today and before
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Last Period Date")
            .setSelection(today) // Set default selection to today
            .setCalendarConstraints(constraintsBuilder)
            .setTheme(R.style.CustomDatePickerTheme)// Apply constraints
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            logd(Date(selectedDateInMillis).toString())
            if (!selectedDateInMillis.isDateWithin9Months()) {
                showToast("Date can't be more than 9 months")
            } else {
                binding.dateText.text = datePicker.headerText
                detailsViewModel.addOrUpdatePregnancyData(
                    PregnancyPeriod(
                        lastPeriodDate = selectedDateInMillis, null, null
                    )
                )
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "Last_Period_Date_Picker")
    }

    override fun onResume() {
        super.onResume()
        handleBackPress {
          popBackStack()
        }
    }
}

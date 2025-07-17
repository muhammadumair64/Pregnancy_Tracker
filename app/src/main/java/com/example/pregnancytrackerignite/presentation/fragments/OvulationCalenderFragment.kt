package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pregnancytrackerignite.databinding.FragmentOvulationCalenderBinding
import com.example.pregnancytrackerignite.presentation.adapters.CalendarAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.popBackStack
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class OvulationCalenderFragment : Fragment() {

    val binding by lazy {
        FragmentOvulationCalenderBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        initCalendar()
        return binding.root
    }

    private fun initCalendar() {
        val date = viewModel.lastPeriodDate
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val lastPeriodDate = LocalDate.parse(date, formatter)

        val cycleLength = (viewModel.periodCycleDays).toInt()
        val ovulationDayIndex = cycleLength - 14

        // Calculate days to display
        val days = mutableListOf<String>()
        val yearMonth = YearMonth.of(lastPeriodDate.year, lastPeriodDate.month)
        val totalDaysInMonth = yearMonth.lengthOfMonth()
        val startDayOfWeek = lastPeriodDate.dayOfWeek.value % 7 // Adjust to start from Sunday

        // Add blank days for alignment
        repeat(startDayOfWeek) { days.add("") }

        // Fill actual month days
        for (i in 1..totalDaysInMonth) {
            days.add(i.toString())
        }

        // Calculate ovulation date and fertile days
        val ovulationDate = lastPeriodDate.plusDays(ovulationDayIndex.toLong())
        val fertileStartDate = ovulationDate.minusDays(2)
        val fertileEndDate = ovulationDate.plusDays(2)
        val fertileDays = listOf(
            ovulationDate.dayOfMonth - 2,
            ovulationDate.dayOfMonth - 1,
            ovulationDate.dayOfMonth + 1,
            ovulationDate.dayOfMonth + 2
        )

        val fertileRange = "${fertileStartDate.format(DateTimeFormatter.ofPattern("MMM d"))} - ${fertileEndDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
        binding.dateToConcive.text = fertileRange

        // Set up the RecyclerView
        binding.tvMonth.text = ovulationDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.calendarRecyclerView.adapter = CalendarAdapter(requireContext(),days, ovulationDate.dayOfMonth, fertileDays)
    }

    private fun initViews() {
        binding.apply {
            backBtn.setOnClickListener {
                popBackStack()
            }
        }
    }

}
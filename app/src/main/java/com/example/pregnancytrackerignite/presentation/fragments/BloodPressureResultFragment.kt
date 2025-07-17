package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.Status
import com.example.pregnancytrackerignite.databinding.FragmentBloodPressureResultBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.iobits.rubik_cube_solver.data.utils.formatDateToHHmm12Hours
import com.iobits.rubik_cube_solver.data.utils.formatToCustomStringInCharts
import com.iobits.rubik_cube_solver.data.utils.groupBarChart
import com.iobits.videocompressor.utils.formatToCustomString
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate


class BloodPressureResultFragment : Fragment() {

    private val binding by lazy {
        FragmentBloodPressureResultBinding.inflate(layoutInflater)
    }
    private val viewModelBloodPressure : BloodPressureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initViews()
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.doneBtn.setOnClickListener {
            safeNavigate(R.id.action_bloodPressureResultFragment_to_mainFragment, R.id.bloodPressureResultFragment)
        }
    }

    private fun initViews() {

        binding.backBtn.setOnClickListener {
            popBackStack()
        }

        MyApplication.tempBpModelToSave.observe(viewLifecycleOwner) {
            binding.apply {
                systolicValue.text = it.systolic.toString()
                diastolicValue.text = it.diastolic.toString()
                pulseValue.text = it.pulse.toString()
                dateText.text = it.date.formatToCustomString()
                seekbar.progress = it.systolic
                statusText.text = getStatus(it.status)
            }
        }

        viewModelBloodPressure.allBpLiveData.observe(viewLifecycleOwner){
            val sortedData = it.sortedBy {
                it.date
            }
            bloodPressureChart(sortedData)
        }

        binding.seekbar.isEnabled = false
        binding.seekbar.setOnTouchListener{ _, _ -> true }  // Returning true prevents all touch events
        binding.seekbar.isFocusable = false
        binding.seekbar.isClickable = false
    }

    private fun getStatus(status: Status): CharSequence {
        return when (status) {
            Status.Normal -> {
                binding.bpLevelMeter.text = "SYS <120 & DIA <80"
                "Normal"
            }
            Status.Hypertension -> {
                binding.bpLevelMeter.text = "SYS <90 & DIA <60"
                "Hypotension"
            }
            Status.ElevatedBP -> {
                binding.bpLevelMeter.text = "SYS 120-129 & DIA <80"
                "Elevated BP"
            }
            Status.HighBpStage1 -> {
                binding.bpLevelMeter.text = "SYS >130 & DIA >80"
                "High Bp Stage 1"
            }
            Status.HighBpStage2 -> {
                binding.bpLevelMeter.text = "SYS >140 & DIA >90"
                "High Bp Stage 2"
            }
        }
    }

    private fun bloodPressureChart(bpModels: List<BpModel>) {
        val systolicValues = ArrayList<BarEntry>()
        val diastolicValues = ArrayList<BarEntry>()
        val pulseValues = ArrayList<BarEntry>()
        val dateLabels = ArrayList<String>()

        for ((index, model) in bpModels.withIndex()) {
            val systolicValue = model.systolic.toFloat()
            val diastolicValue = model.diastolic.toFloat()
            val pulseValue = model.pulse.toFloat()
            systolicValues.add(BarEntry(index.toFloat(), systolicValue))
            diastolicValues.add(BarEntry(index.toFloat(), diastolicValue))
            pulseValues.add(BarEntry(index.toFloat(), pulseValue))
            val formattedDate = model.date.formatToCustomStringInCharts() ?: "Unknown"
            val formattedTime = model.time.formatDateToHHmm12Hours() ?: "Unknown"
            // Add date labels to the list
            dateLabels.add("$formattedDate ($formattedTime)")
        }

        val incomeDataSet = BarDataSet(systolicValues, "Systolic")
        incomeDataSet.color = ContextCompat.getColor(requireContext(), R.color.theme_text_color)
        val expenseDataSet = BarDataSet(diastolicValues, "Diastolic")
        expenseDataSet.color = ContextCompat.getColor(requireContext(), R.color.yellow)
        val pulseDataSet = BarDataSet(pulseValues, "Pulse")
        pulseDataSet.color = ContextCompat.getColor(requireContext(), R.color.gray_text)
        groupBarChart(
            incomeDataSet,
            expenseDataSet,
            systolicValues,
            dateLabels,
            binding.barChart,
            pulseDataSet
        )
    }

}
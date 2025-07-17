package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.databinding.FragmentBloodPressureHistoryBinding
import com.example.pregnancytrackerignite.presentation.adapters.BpHistoryRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.iobits.rubik_cube_solver.data.utils.formatDateToHHmm12Hours
import com.iobits.rubik_cube_solver.data.utils.formatToCustomStringInCharts
import com.iobits.rubik_cube_solver.data.utils.groupBarChart
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate


class BloodPressureHistoryFragment : Fragment() {
    val binding by lazy {
        FragmentBloodPressureHistoryBinding.inflate(layoutInflater)
    }
    private var mList = ArrayList<BpModel>()
    private val viewModelBloodPressure by activityViewModels<BloodPressureViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }



    fun initViews(){

        val mAdapter = BpHistoryRvAdapter(requireContext())
        binding.historyRv.apply {
         adapter = mAdapter
         layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        binding.apply {
            backBtn.setOnClickListener {
                popBackStack()
            }
            recordBtn.setOnClickListener {
                safeNavigate(R.id.action_bloodPressureHistoryFragment_to_bloodPressureRecordFragment,R.id.bloodPressureHistoryFragment)
            }
        }
        viewModelBloodPressure.allBpLiveData.observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                binding.placeholder.visibility = View.VISIBLE
                safeNavigate(R.id.action_bloodPressureHistoryFragment_to_bloodPressureRecordFragment,R.id.bloodPressureHistoryFragment)
            }else{
                binding.placeholder.visibility = View.GONE
                mList.clear()
                mList.addAll(it)
                mAdapter.updateList(mList)
            }

            val sortedData = it.sortedBy {
                it.date
            }
            bloodPressureChart(sortedData)
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
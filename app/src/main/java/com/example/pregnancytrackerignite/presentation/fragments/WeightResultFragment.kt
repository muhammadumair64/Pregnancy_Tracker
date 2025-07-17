package com.example.pregnancytrackerignite.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentWeightResultBinding
import com.example.pregnancytrackerignite.databinding.FragmentWeightTrackerBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.iobits.videocompressor.utils.popBackStack
import kotlin.math.pow
import kotlin.math.round


class WeightResultFragment : Fragment() {
val binding by lazy {
    FragmentWeightResultBinding.inflate(layoutInflater)
}
    private val TAG = "WeightResultFragment"
    val viewModel : SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }
    fun initViews(){
        val gainWeight = viewModel.currentWeight.toFloat() - viewModel.preWeight.toFloat()
        binding.apply {
            weightRange.text = "${gainWeight-5} - ${gainWeight+5} KG"
            try {
                preWeight.text = "${calculateBMI(viewModel.preWeight.toDouble(),viewModel.height.toDouble())}"
            } catch (e:Exception){
                Log.d(TAG, "initViews: ${e.localizedMessage}")
            }
            backBtn.setOnClickListener {
                popBackStack()
            }
            startBtn.setOnClickListener {
                popBackStack()
            }
        }
        lineChart()
    }

    private fun calculateBMI(weightBeforePregnancy: Double, heightInCm: Double): Double {
        val heightInMeters = heightInCm / 100.0
        val bmi = weightBeforePregnancy / (heightInMeters.pow(2))
        return round(bmi * 100) / 100.0
    }

    private fun lineChart() {

        val lineChart = binding.lineChart

        // Create sample data entries
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 0f))
        entries.add(Entry(1f, viewModel.preWeight.toFloat()))
        entries.add(Entry(2f, viewModel.currentWeight.toFloat()))

        // Create a dataset and customize it
        val dataSet = LineDataSet(entries, "Weight")
        dataSet.apply {
            color = ContextCompat.getColor(requireContext(), R.color.light_pink)
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.light_pink))
            lineWidth = 2f
            circleRadius = 4f
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.line_chart_gradient)
        }

        // Customize X-axis to remove vertical grid lines
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false) // Remove vertical grid lines

        // Customize Y-axis to show horizontal grid lines only on the left
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(true) // Show horizontal grid lines
        yAxisLeft.enableGridDashedLine(10f, 10f, 0f) // Optional: make grid lines dashed
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false // Disable right Y-axis

        // Create and set LineData
        val lineData = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.data = lineData

        // Invalidate the chart to refresh
        lineChart.invalidate()
    }
}

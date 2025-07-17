package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.databinding.FragmentCounterBinding
import com.example.pregnancytrackerignite.presentation.viewModel.KickNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.KickRecordViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.videocompressor.utils.getCurrentDateInMM
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.singleToast

class CounterFragment : Fragment() {

    val binding by lazy {
        FragmentCounterBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()
    val kickViewModel: KickRecordViewModel by activityViewModels()
    var kickCount = 0
    var lastDate= ""
    var lastRecord :KickDataClass? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        performNavigation()
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun performNavigation() {
        viewModel.kickCounterNavSelected = {
            when (it) {
                KickNavEvents.NavigateToCounter -> {}
                KickNavEvents.NavigateToRecords -> {
                    safeNavigate(
                        R.id.action_counterFragment_to_recordsFragment,
                        R.id.counterFragment
                    )
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.apply {
            kickBtn.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                    }
                }
                false
            }
            kickBtn.setOnClickListener {
                kickCount++
                if (kickCount <= 10) {
                    progress.progress = kickCount.toFloat()
                    progressText.text = String.format("%02d", kickCount)
                } else {
                    requireContext().singleToast("Only 10 kick allowed in one hour")
                }
            }
        }
        kickViewModel.latestReportModel.observe(viewLifecycleOwner) { record ->
            try {
                if(record.date.isNotEmpty()){
                    lastRecord = record
                    lastDate = record.date
                    if (getCurrentDateInMM() == record.date) {
                        binding.todayTotal.text = record.kickCount
                    } else {
                        binding.todayTotal.text = "00"
                    }
                }
            }catch (e:Exception)
            {e.localizedMessage}
        }
        binding.save.setOnClickListener {
            val mDate = getCurrentDateInMM()
            if(((lastRecord?.kickCount?.toInt()?.plus(kickCount)) ?: 0) < 121){
                if(lastDate != mDate){
                    if (kickCount > 0 ) {
                        kickViewModel.addReportModel(
                            KickDataClass(
                                0,
                                String.format("%02d", kickCount),
                                mDate
                            )
                        )
                        reset()
                    }
                }
                else{
                    if (kickCount > 0 && lastRecord != null) {
                        kickViewModel.addReportModel(
                            KickDataClass(
                                lastRecord!!.id,
                                String.format("%02d", kickCount+lastRecord!!.kickCount.toInt()),
                                mDate

                            )
                        )
                        reset()
                    }
                }
            }else{
                requireContext().singleToast("Only 120 kick allowed in one Day")
            }

        }

        binding.reset.setOnClickListener {
      reset()
        }
    }
    private fun reset(){
        kickCount = 0
        binding.progress.progress = 0f
        binding.progressText.text = "00"
    }
}

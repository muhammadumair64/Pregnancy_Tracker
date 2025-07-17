package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentKickCounterBinding
import com.example.pregnancytrackerignite.presentation.viewModel.KickNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.invisible
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.visible


class KickCounterFragment : Fragment() {
    val binding by lazy {
        FragmentKickCounterBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        handleBackPress {
            popBackStack()
        }
    }

    fun initViews() {
        binding.apply {
            footTab.setOnClickListener { setTabBar(1) }
            recordsTab.setOnClickListener { setTabBar(2) }
            backBtn.setOnClickListener { popBackStack() }
        }
    }

    private fun setTabBar(selection: Int) {
        binding.apply {
            footIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            recordsIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            footText.setTextColor(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            recordsText.setTextColor(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            footIndicator.invisible()
            recordIndicator.invisible()

            when (selection) {
                1 -> {
                    viewModel.kickCounterNavSelected?.invoke(KickNavEvents.NavigateToCounter)
                    footIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary))
                    footText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    footIndicator.visible()
                }
                2 -> {
                    viewModel.kickCounterNavSelected?.invoke(KickNavEvents.NavigateToRecords)
                    recordsIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary))
                    recordsText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    recordIndicator.visible()
                }
            }
        }
    }
}

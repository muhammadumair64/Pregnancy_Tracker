package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentToolsBinding
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.safeNavigate

class ToolsFragment : Fragment() {

    val binding by lazy {
        FragmentToolsBinding.inflate(layoutInflater)
    }
    private val viewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        performNavigation()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            viewModel.clickCallBack?.apply {
                settings.setOnClickListener {
                    invoke(ClickEvents.ClickOnSettings)
                }
                bloodPressureCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnBP)
                }
                scanCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnScans)
                }
                babySizeCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnBabySize)
                }
                reportCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnReport)
                }
                breathingCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnLungsExercise)
                }
                kickCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnKickCounter)
                }
                ovulationCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnOvulation)
                }
                weightCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnWeightTracker)
                }
                mediCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnMedicineReminder)
                }
                babyNamesCard.setOnClickListener {
                    invoke(ClickEvents.ClickOnBabyNames)
                }
            }
        }
    }

    private fun performNavigation() {
        viewModel.handleNavUIStates?.invoke(NavEvents.NavigateToTools)
        viewModel.onNavItemSelected = {
            when (it) {
                NavEvents.NavigateToHome -> {safeNavigate(R.id.action_toolsFragment_to_homeFragment2,R.id.toolsFragment)}
                NavEvents.NavigateToDiet -> {safeNavigate(R.id.action_toolsFragment_to_dietPlanFragment,R.id.toolsFragment)}
                NavEvents.NavigateToTools -> {}
                NavEvents.NavigateToExercise -> { safeNavigate(R.id.action_toolsFragment_to_exerciseFragment,R.id.toolsFragment)}
            } }
    }
}

package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentMainBinding
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.safeNavigate

class MainFragment : Fragment() {
    val binding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        viewModel.handleNavUIStates = {
            when (it) {
                NavEvents.NavigateToHome -> binding.bottomNavigation.setItemSelected(
                    R.id.home,
                    true
                )

                NavEvents.NavigateToDiet -> binding.bottomNavigation.setItemSelected(
                    R.id.diet,
                    true
                )

                NavEvents.NavigateToTools -> binding.bottomNavigation.setItemSelected(
                    R.id.tools,
                    true
                )

                NavEvents.NavigateToExercise -> binding.bottomNavigation.setItemSelected(
                    R.id.exercise,
                    true
                )
            }
        }
        binding.apply {
            bottomNavigation.setOnItemSelectedListener {
                when (it) {
                    R.id.home -> {
                        viewModel.onNavItemSelected?.invoke(NavEvents.NavigateToHome)
                    }

                    R.id.tools -> {
                        viewModel.onNavItemSelected?.invoke(NavEvents.NavigateToTools)
                    }

                    R.id.exercise -> {
                        viewModel.onNavItemSelected?.invoke(NavEvents.NavigateToExercise)
                    }

                    R.id.diet -> {
                        viewModel.onNavItemSelected?.invoke(NavEvents.NavigateToDiet)
                    }
                }
            }

            viewModel.clickCallBack = {
                when (it) {
                    ClickEvents.ClickOnPrediction -> safeNavigate(R.id.action_mainFragment_to_genderPrediction, R.id.mainFragment)
                    ClickEvents.ClickOnBP -> safeNavigate(R.id.action_mainFragment_to_bloodPressureHistoryFragment, R.id.mainFragment)
                    ClickEvents.ClickExercise -> safeNavigate(R.id.action_mainFragment_to_exerciseDetailFragment, R.id.mainFragment)
                    ClickEvents.ClickBlog -> safeNavigate(R.id.action_mainFragment_to_blogDetailsFragment, R.id.mainFragment)
                    ClickEvents.ClickOnSettings -> safeNavigate(R.id.action_mainFragment_to_settingsFragment, R.id.mainFragment)
                    ClickEvents.ClickOnScans -> safeNavigate(R.id.action_mainFragment_to_scansFragment, R.id.mainFragment)
                    ClickEvents.ClickOnDueDate -> safeNavigate(R.id.action_mainFragment_to_dueDateFragment, R.id.mainFragment)
                    ClickEvents.ClickOnBabySize -> safeNavigate(R.id.action_mainFragment_to_babySizeFragment, R.id.mainFragment)
                    ClickEvents.ClickOnReport -> safeNavigate(R.id.action_mainFragment_to_reportsFragment, R.id.mainFragment)
                    ClickEvents.ClickOnLungsExercise -> safeNavigate(R.id.action_mainFragment_to_lungsExerciseFragment, R.id.mainFragment)
                    ClickEvents.ClickOnKickCounter -> safeNavigate(R.id.action_mainFragment_to_kickCounterFragment, R.id.mainFragment)
                    ClickEvents.ClickOnOvulation -> safeNavigate(R.id.action_mainFragment_to_ovulationFragment, R.id.mainFragment)
                    ClickEvents.ClickOnWeightTracker -> safeNavigate(R.id.action_mainFragment_to_weightTrackerFragment, R.id.mainFragment)
                    ClickEvents.ClickOnMedicineReminder -> safeNavigate(R.id.action_mainFragment_to_medicineReminderFragment, R.id.mainFragment)
                    ClickEvents.ClickAiDoctor -> safeNavigate(R.id.action_mainFragment_to_AIDoctorPromptFragment, R.id.mainFragment)
                    ClickEvents.ClickOnMeal -> safeNavigate(R.id.action_mainFragment_to_mealFragment, R.id.mainFragment)
                    ClickEvents.ClickOnViewAll -> safeNavigate(R.id.action_mainFragment_to_blogsFragment, R.id.mainFragment)
                    ClickEvents.ClickOnBabyNames -> safeNavigate(R.id.action_mainFragment_to_babyNameFragment, R.id.mainFragment)
                    ClickEvents.ClickOnTrimester -> safeNavigate(R.id.action_mainFragment_to_trimesterChartFragment,R.id.mainFragment)
                    ClickEvents.ClickOnMainImage -> safeNavigate(R.id.action_mainFragment_to_babyImageGridFragment,R.id.mainFragment)
                }
            }
        }
    }

}

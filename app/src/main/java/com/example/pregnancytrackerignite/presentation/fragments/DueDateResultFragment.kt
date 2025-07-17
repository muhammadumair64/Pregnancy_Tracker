package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentDueDateResultBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.rubik_cube_solver.data.utils.navigateSafe
import com.iobits.videocompressor.utils.handleBackPress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DueDateResultFragment : Fragment() {

    private val binding by lazy {
        FragmentDueDateResultBinding.inflate(layoutInflater)
    }

    private val viewModel: SharedViewModel by activityViewModels()

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
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        lifecycleScope.launch {
            viewModel.pregnancyData.collectLatest { user ->
                user?.let {
                    binding.apply {
                        firstTxtWeek.text = "${user.gestationalAge?.weeks} Weeks"
                        remainingWeeks.text = "${
                            user.gestationalAge?.weeks?.let { it1 ->
                                calculateRemainingPregnancyWeeks(it1)
                            }
                        } Weeks"
                        startBtn.setOnClickListener {
//                            navigateSafe(
//                                R.id.action_dueDateResultFragment_to_homeFragment,
//                                R.id.dueDateResultFragment
//                            )
                            if (viewModel.isFromSettings) {
                                viewModel.isFromSettings = false
                                navigateSafe(
                                    R.id.action_dueDateResultFragment_to_mainFragment,
                                    R.id.dueDateResultFragment
                                )
                            } else {
                                navigateSafe(
                                    R.id.action_dueDateResultFragment_to_genderPrediction,
                                    R.id.dueDateResultFragment
                                )
                            }
                        }
                    }
                    logd(Date(user.estimatedDate!!).toString())
                    logd((user.gestationalAge!!).toString())
                    logd(Date(user.lastPeriodDate!!).toString())
                } ?: run {
                    logd("No Data found")
                }
            }
        }
    }

    private fun calculateRemainingPregnancyWeeks(currentWeeks: Int): Int {
        val totalPregnancyWeeks = 40
        return if (currentWeeks in 0..totalPregnancyWeeks) {
            totalPregnancyWeeks - currentWeeks
        } else {
            throw IllegalArgumentException("Invalid number of weeks. It should be between 0 and $totalPregnancyWeeks.")
        }
    }
}

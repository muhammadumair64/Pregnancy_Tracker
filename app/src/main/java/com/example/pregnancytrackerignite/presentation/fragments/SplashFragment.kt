package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.presentation.viewModel.PregnancyViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.rubik_cube_solver.data.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var isUserPresent: Boolean = false
    private var isDataPresent: Boolean = false
    private val viewModel: SharedViewModel by activityViewModels()
    private val pregnancyViewModel: PregnancyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collectLatest { user ->
                    user?.let {
                        logd(it.toString())
                        isUserPresent = true
                    } ?: run {
                        logd("No User found")
                    }
                }
            }
        }

        lifecycleScope.launch {
                 val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val current= Calendar.FRIDAY

            Log.d("SplashScreen", "onCreateView: $currentDayOfWeek ===== $current")
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pregnancyData.collectLatest { user ->
                    user?.let {
                        logd(it.toString())
                        isDataPresent = true
                        if (user.enteringDate != Date()) {
                            pregnancyViewModel.addOrUpdatePregnancyData(
                                user.copy(
                                    lastPeriodDate = user.lastPeriodDate,
                                    id = user.id,
                                    estimatedDate = null,
                                    gestationalAge = null
                                )
                            )
                        }
                    } ?: run {
                        logd("No Data found")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                delay(5000)

                    if (isUserPresent && isDataPresent) {
                        navigateSafe (
                            R.id.action_splashFragment_to_dueDateResultFragment,
                            R.id.splashFragment
                        )
                    } else if (isUserPresent) {
                        navigateSafe(
                            R.id.action_splashFragment_to_dueDateFragment,
                            R.id.splashFragment
                        )
                    } else {
                        navigateSafe(
                            R.id.action_splashFragment_to_loginFragment, R.id.splashFragment
                        )

                    }
            }
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

}
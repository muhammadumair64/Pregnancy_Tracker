package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.dataExercises
import com.example.pregnancytrackerignite.databinding.FragmentExerciseBinding
import com.example.pregnancytrackerignite.presentation.adapters.ExerciseRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.dpToPx
import com.iobits.videocompressor.utils.safeNavigate

class ExerciseFragment : Fragment() {

    val binding by lazy {
        FragmentExerciseBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()
    private var adapter: ExerciseRvAdapter? = null

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
            val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    // Detect upward swipe
                    if (distanceY > 0) {
                        // Scrolling up, add bottom margin
                        val params = exerciseRv.layoutParams as? ViewGroup.MarginLayoutParams ?: return false
                        params.bottomMargin = 100.dpToPx()
                        exerciseRv.layoutParams = params
                    } else if (distanceY < 0) {
                        // Scrolling down, remove bottom margin
                        val params = exerciseRv.layoutParams as? ViewGroup.MarginLayoutParams ?: return false
                        params.bottomMargin = 0
                        exerciseRv.layoutParams = params
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })
            exerciseRv.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                false
            }

            adapter = ExerciseRvAdapter(requireContext(), dataExercises) { selectedExercise ->
                viewModel.selectedExerciseItem = selectedExercise
                viewModel.clickCallBack?.invoke(ClickEvents.ClickExercise)
            }
            exerciseRv.adapter = adapter
            adapter?.notifyDataSetChanged()

            settings.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnSettings)
            }
        }
    }

    private fun performNavigation() {
        viewModel.handleNavUIStates?.invoke(NavEvents.NavigateToExercise)
        viewModel.onNavItemSelected = {
            when (it) {
                NavEvents.NavigateToHome -> {safeNavigate(R.id.action_exerciseFragment_to_homeFragment2,R.id.exerciseFragment)}
                NavEvents.NavigateToDiet ->  {safeNavigate(R.id.action_exerciseFragment_to_dietPlanFragment,R.id.exerciseFragment)}
                NavEvents.NavigateToTools -> {safeNavigate(R.id.action_exerciseFragment_to_toolsFragment,R.id.exerciseFragment)}
                NavEvents.NavigateToExercise -> {}
            }
        }
    }
}

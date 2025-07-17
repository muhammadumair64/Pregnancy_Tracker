package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.utils.Meal
import com.example.pregnancytrackerignite.data.utils.ScansHelper
import com.example.pregnancytrackerignite.data.utils.weakDietPlan
import com.example.pregnancytrackerignite.databinding.FragmentDietPlanBinding
import com.example.pregnancytrackerignite.presentation.adapters.DietDayRvAdapter
import com.example.pregnancytrackerignite.presentation.adapters.MealPlanRvAdapter
import com.example.pregnancytrackerignite.presentation.adapters.WeekRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.dpToPx
import com.iobits.videocompressor.utils.safeNavigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class DietPlanFragment : Fragment() {
    val binding by lazy {
        FragmentDietPlanBinding.inflate(layoutInflater)
    }
    var mealList = ArrayList<Meal>()
    val TAG = "DietPlanFragment"
    val viewModel: SharedViewModel by activityViewModels()
    private var mList : ArrayList<String> = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        performNavigation()
        return binding.root
    }
@SuppressLint("ClickableViewAccessibility")
fun initViews() {
    binding.apply {
        settings.setOnClickListener {
            viewModel.clickCallBack?.invoke(ClickEvents.ClickOnSettings)
        }

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
                    val params = dietRv.layoutParams as? ViewGroup.MarginLayoutParams ?: return false
                    params.bottomMargin = 100.dpToPx()
                    dietRv.layoutParams = params
                } else if (distanceY < 0) {
                    // Scrolling down, remove bottom margin
                    val params = dietRv.layoutParams as? ViewGroup.MarginLayoutParams ?: return false
                    params.bottomMargin = 0
                    dietRv.layoutParams = params
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })

        dietRv.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
      val mAdapter =  MealPlanRvAdapter(requireContext()) { it ->
          viewModel.selectedMeal = it
          viewModel.clickCallBack?.invoke(ClickEvents.ClickOnMeal)
        }


        lifecycleScope.launch {
            mList.clear()
            addMeals(0)
            mList.addAll(getSevenDayDates())
            weekRv.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                 adapter = DietDayRvAdapter(requireContext(), mList) { it ->
                     lifecycleScope.apply {
                         addMeals(it)
                         mAdapter.updateList(mealList)
                     }
                 }
                mAdapter.updateList(mealList)
            }
            weekRv.smoothScrollToPosition(0)
            dietRv.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
                // Attach the gesture detector to the RecyclerView
              setOnTouchListener { _, event ->
                    gestureDetector.onTouchEvent(event)
                    false // Allow RecyclerView to handle other touch events
                }
            }
        }
    }
}

    private fun getSevenDayDates(): ArrayList<String> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val dates = ArrayList<String>()

        for (i in 0 until 7) {
            when (i) {
                0 -> dates.add("Today")
                1 -> dates.add("Tomorrow")
                else -> dates.add(dateFormat.format(calendar.time))
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
        }
        return dates
    }

    private fun performNavigation() {
        viewModel.handleNavUIStates?.invoke(NavEvents.NavigateToDiet)
        viewModel.onNavItemSelected = {
            when (it) {
                NavEvents.NavigateToHome -> safeNavigate(
                    R.id.action_dietPlanFragment_to_homeFragment2,
                    R.id.dietPlanFragment
                )
                NavEvents.NavigateToTools -> safeNavigate(
                    R.id.action_dietPlanFragment_to_toolsFragment,
                    R.id.dietPlanFragment
                )
                NavEvents.NavigateToExercise -> safeNavigate(
                    R.id.action_dietPlanFragment_to_exerciseFragment,
                    R.id.dietPlanFragment
                )
                NavEvents.NavigateToDiet -> {}
            }
        }
    }

    private fun addMeals(index:Int){
        mealList.clear()
        mealList.add(weakDietPlan.planList[index].breakfast)
        mealList.add(weakDietPlan.planList[index].snack1)
        mealList.add(weakDietPlan.planList[index].lunch)
        mealList.add(weakDietPlan.planList[index].snack2)
        mealList.add(weakDietPlan.planList[index].dinner)
    }
}
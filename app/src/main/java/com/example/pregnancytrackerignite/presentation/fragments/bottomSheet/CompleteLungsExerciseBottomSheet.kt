package com.example.pregnancytrackerignite.presentation.fragments.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.LungsExerciseCompleteBtmsheetBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CompleteLungsExerciseBottomSheet(
    private val onRetry: () -> Unit,
    private val setReminder: ()-> Unit
) : BottomSheetDialogFragment() {

    private val binding by lazy {
        LungsExerciseCompleteBtmsheetBinding.inflate(layoutInflater)
    }
    private var timeInMin = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dialog?.setCancelable(false)
        binding.apply {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_END
            val layoutManagerResting = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            breathing1Min.setOnClickListener {
                timeInMin = 1
                resetAll()
                breathing1Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_color_round_corner)
                breathing1Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            breathing2Min.setOnClickListener {
                timeInMin = 2
                resetAll()
                breathing2Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_color_round_corner)
                breathing2Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            breathing3Min.setOnClickListener {
                timeInMin = 3
                resetAll()
                breathing3Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_color_round_corner)
                breathing3Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            breathing4Min.setOnClickListener {
                timeInMin = 4
                resetAll()
                breathing4Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_color_round_corner)
                breathing4Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            breathing5Min.setOnClickListener {
                timeInMin = 5
                resetAll()
                breathing5Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_color_round_corner)
                breathing5Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            retryBtn.setOnClickListener {
                dismiss()
                onRetry.invoke()
            }

            remindLaterBtn.setOnClickListener {
                dismiss()
                setReminder.invoke()
            }
        }

        return binding.root
    }

    private fun resetAll(){
        binding.apply {
            breathing1Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.stroke_border_color_light)
            breathing2Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.stroke_border_color_light)
            breathing3Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.stroke_border_color_light)
            breathing4Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.stroke_border_color_light)
            breathing5Min.background = ContextCompat.getDrawable(requireContext(), R.drawable.stroke_border_color_light)

            breathing1Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
            breathing2Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
            breathing3Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
            breathing4Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
            breathing5Min.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
        }
    }

    /**if want a static height or change the behavior of the sheet like can drag up down etc*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screenHeight = resources.displayMetrics.heightPixels
        val desiredHeight = (screenHeight) * 0.5
        val bottomSheetLayout = binding.mainLayout
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        // Set the desired height to the layout
        val layoutParams = bottomSheetLayout.layoutParams
        layoutParams.height = desiredHeight.toInt()
        bottomSheetLayout.layoutParams = layoutParams
    }
    override fun onResume() {
        super.onResume()
    }
}
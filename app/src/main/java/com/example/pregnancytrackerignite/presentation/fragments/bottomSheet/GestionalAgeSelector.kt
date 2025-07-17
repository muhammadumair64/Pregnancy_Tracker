package com.example.pregnancytrackerignite.presentation.fragments.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pregnancytrackerignite.data.models.GestationalAge
import com.example.pregnancytrackerignite.databinding.AgeselectorbottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GestionalAgeSelector(private val onValueSelected: (GestationalAge) -> Unit) :
    BottomSheetDialogFragment() {
    private val binding by lazy {
        AgeselectorbottomsheetBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
         binding.apply {
            continueBtn.setOnClickListener {
                onValueSelected.invoke(GestationalAge(weeksPicker.value, daysPicker.value))
                dismiss()
            }
        }
        return binding.root
    }

    /**if want a static height or change the behavior of the sheet like can drag up down etc*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screenHeight = resources.displayMetrics.heightPixels
        val desiredHeight = (screenHeight)*0.75
        val bottomSheetLayout = binding.mainLayout
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
        // Set the desired height to the layout
        val layoutParams = bottomSheetLayout.layoutParams
        layoutParams.height = desiredHeight.toInt()
        bottomSheetLayout.layoutParams = layoutParams

    }
}
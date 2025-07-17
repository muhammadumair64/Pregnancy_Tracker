package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentWeightTrackerBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.hideKeyboard
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.toEditable

class WeightTrackerFragment : Fragment() {

    val binding by lazy {
        FragmentWeightTrackerBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()
    private var isChecked = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            weekEt.text = viewModel.currentWeek.toEditable()
            weekEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    weekBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    weekBg.setBackgroundResource(R.drawable.field_bg)
                }
            }

            preWeightEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    preWeightBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    preWeightBg.setBackgroundResource(R.drawable.field_bg)
                }
            }

            currentWeightEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    currentWeightBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    currentWeightBg.setBackgroundResource(R.drawable.field_bg)
                }
            }
            heightEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    heightBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    heightBg.setBackgroundResource(R.drawable.field_bg)
                }
            }

            checkbox.setOnClickListener {
                hideKeyboard(it)
                if (isChecked) {
                    Glide.with(requireContext()).load(R.drawable.unselected).into(checkbox)
                } else {
                    Glide.with(requireContext()).load(R.drawable.checked).into(checkbox)
                }
                isChecked = !isChecked
                checkConditionsAndUpdateButton()
            }

            startBtn.setOnClickListener {
                if (heightEt.text.isNotEmpty() && currentWeightEt.text.isNotEmpty() && preWeightEt.text.isNotEmpty()) {
                    viewModel.apply {
                        preWeight = preWeightEt.text.toString()
                        currentWeight = currentWeightEt.text.toString()
                        height = heightEt.text.toString()
                    }
                    safeNavigate(R.id.action_weightTrackerFragment_to_weightResultFragment,R.id.weightTrackerFragment)
                } else {
                    showToast("Fill the fields first...")
                }
            }
            backBtn.setOnClickListener {
                popBackStack()
            }


            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkConditionsAndUpdateButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            currentWeightEt.addTextChangedListener(textWatcher)
            heightEt.addTextChangedListener(textWatcher)
            preWeightEt.addTextChangedListener(textWatcher)
        }
    }

    private fun checkConditionsAndUpdateButton() {
        val currentWeightEt = binding.currentWeightEt.text.toString()
        val heightEt = binding.heightEt.text.toString()
        val preWeightEt = binding.preWeightEt.text.toString()

        binding.apply {
            if (preWeightEt.length > 1 && currentWeightEt.length > 1 && heightEt.length > 1) {
                startBtn.alpha = 1f // or set the background directly if you have different drawable
                startBtn.isClickable = true
            } else {
                startBtn.alpha = 0.5f
                startBtn.isClickable = true
            }
        }
    }
}

package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentPromptsBinding
import com.example.pregnancytrackerignite.presentation.viewModel.AiDoctorViewModel
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showKeyboard
import com.iobits.videocompressor.utils.singleToast
import com.iobits.videocompressor.utils.toEditable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AIDoctorPromptFragment : Fragment() {

    val binding by lazy {
        FragmentPromptsBinding.inflate(layoutInflater)
    }
    private val viewModel: AiDoctorViewModel by activityViewModels()
    private var currentLineCount = 1
    private val textToDisplay = "How are you Feeling\nToday?"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    fun initViews(){
        startTypingAnimation(textToDisplay)
binding.backBtn.setOnClickListener {
    popBackStack()
}
        binding.promptEt.requestFocus()
        showKeyboard(binding.promptEt)

        // Adjust text size based on the number of lines
        binding.promptEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adjustTextSize(binding.promptEt)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.startBtn.setOnClickListener {
            if(binding.promptEt.text.length > 2){
                viewModel.prompt = binding.promptEt.text.toString()
                 safeNavigate(R.id.action_AIDoctorPromptFragment_to_aiDoctorFragment,R.id.AIDoctorPromptFragment)
            }else{
                requireContext().singleToast("Please Type something")
            }
        }
    }

    private fun startTypingAnimation(text: String) {
       lifecycleScope.launch {
            binding.title.text = ""
           for (char in text) {
               binding.title.append(char.toString())
               delay(75) // Delay for the typing effect
           }
        }
    }

    private fun showKeyboard(editText: EditText) {
        // Use a Handler to ensure the keyboard shows after the view is ready
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }, 200) // Delay to ensure the layout is ready
    }

    private fun adjustTextSize(editText: EditText) {
        val newLineCount = editText.lineCount

        // Check if the line count has increased
        if (newLineCount > currentLineCount) {
            currentLineCount = newLineCount // Update the current line count

            when (newLineCount) {
                2 -> editText.textSize = 20f // Second line: 16sp
                3 -> editText.textSize = 17f // Third line or more: 14sp
            }
        }
    }
}
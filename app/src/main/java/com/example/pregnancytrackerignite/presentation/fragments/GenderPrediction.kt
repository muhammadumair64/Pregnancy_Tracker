package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.GenderPredictionQuestion
import com.example.pregnancytrackerignite.data.models.GenderPredictionResult
import com.example.pregnancytrackerignite.data.utils.GenderPredictionUtils
import com.example.pregnancytrackerignite.databinding.FragmentGenderPredictionBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showPredictionDialog
import com.iobits.videocompressor.utils.toast
import kotlinx.coroutines.launch


class GenderPrediction : Fragment() {
    private val binding by lazy {
        FragmentGenderPredictionBinding.inflate(layoutInflater)
    }
    private var selectedAnswer = 0
    private var gender = ""
    private var solvedQuestions = 0
    private var totalQuestions = 20
    private var boyPoints = 0
    private var girlPoints = 0
    var dialogCount = 1
    val viewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.skip.setOnClickListener {
            safeNavigate(R.id.action_genderPrediction_to_mainFragment, R.id.genderPrediction)
        }
    }

    private fun initViews() {
        lifecycleScope.launch {
            viewModel.genderResult.collect { result ->
                gender = result?.gender.toString()
                solvedQuestions = result?.solvedQuestion ?: 0
                val temp = result?.totalQuestions ?: 20
                totalQuestions = if (temp != 0) {
                    temp
                } else {
                    20
                }
                boyPoints = result?.boyPoints ?: 0
                girlPoints = result?.girlPoints ?: 0
                disableChecks()
                setQuestion()
                selectedAnswer = 0
            }
        }
        binding.apply {
            layout1.setOnClickListener { makeQuestionSelected(1) }
            layout2.setOnClickListener { makeQuestionSelected(2) }
            layout3.setOnClickListener { makeQuestionSelected(3) }
            layout4.setOnClickListener { makeQuestionSelected(4) }
        }
        binding.nextBtn.setOnClickListener {
            if (selectedAnswer != 0) {
                calculatePoints()
                showPredictionDialog()
            }
        }
    }

    private fun setQuestion() {
        binding.apply {
            try {
                if(solvedQuestions == GenderPredictionUtils.questions.size) {
                    safeNavigate(R.id.action_genderPrediction_to_mainFragment, R.id.genderPrediction)
                   // Toast.makeText(requireContext(), "Well done, Quiz Complete", Toast.LENGTH_SHORT).show()
                }
                question.text = GenderPredictionUtils.questions[solvedQuestions].question
                ans1.text = GenderPredictionUtils.questions[solvedQuestions].option1
                ans2.text = GenderPredictionUtils.questions[solvedQuestions].option2
                ans3.text = GenderPredictionUtils.questions[solvedQuestions].option3
                ans4.text = GenderPredictionUtils.questions[solvedQuestions].option4

                gpProgress.setProgress(solvedQuestions)
                textView6 .text = "Question ${solvedQuestions+1} of 20"
            }catch (e:Exception){
                e.localizedMessage
            }
        }
    }

    private fun calculatePoints() {
        if (selectedAnswer == 1 || selectedAnswer == 3) {
            boyPoints++
        } else {
            girlPoints++
        }
        gender = if (boyPoints > girlPoints) {
            "Boy"
        } else {
            "Girl"
        }
        viewModel.setGenderData(
            GenderPredictionResult(
                gender,
                solvedQuestions + 1,
                totalQuestions,
                boyPoints,
                girlPoints
            )
        )
    }

    private fun makeQuestionSelected(option: Int) {
        binding.apply {
            disableChecks()
            nextBtn.alpha = 1f
            when (option) {
                1 -> {
                    selectedAnswer = 1
                    check1.setImageResource(R.drawable.round_check)
                    layout1.setBackgroundResource(R.drawable.item_question_pink_bg)
                }

                2 -> {
                    selectedAnswer = 2
                    check2.setImageResource(R.drawable.round_check)
                    layout2.setBackgroundResource(R.drawable.item_question_pink_bg)
                }

                3 -> {
                    selectedAnswer = 3
                    check3.setImageResource(R.drawable.round_check)
                    layout3.setBackgroundResource(R.drawable.item_question_pink_bg)
                }

                4 -> {
                    selectedAnswer = 4
                    check4.setImageResource(R.drawable.round_check)
                    layout4.setBackgroundResource(R.drawable.item_question_pink_bg)
                }
            }
        }
    }

    private fun disableChecks() {
        binding.apply {
            nextBtn.alpha = 0.5f
            check1.setImageResource(R.drawable.round_unchecked)
            check2.setImageResource(R.drawable.round_unchecked)
            check3.setImageResource(R.drawable.round_unchecked)
            check4.setImageResource(R.drawable.round_unchecked)
            layout1.setBackgroundResource(R.drawable.item_question_stroke_bg)
            layout2.setBackgroundResource(R.drawable.item_question_stroke_bg)
            layout3.setBackgroundResource(R.drawable.item_question_stroke_bg)
            layout4.setBackgroundResource(R.drawable.item_question_stroke_bg)
        }
    }

    private fun showPredictionDialog(){
        if(dialogCount % 5 == 0){
            dialogCount = 1
            showPredictionDialog(requireActivity(),gender,solvedQuestions+1,totalQuestions){
                safeNavigate(R.id.action_genderPrediction_to_mainFragment, R.id.genderPrediction)
            }
        }else{
            dialogCount++
        }
    }
}

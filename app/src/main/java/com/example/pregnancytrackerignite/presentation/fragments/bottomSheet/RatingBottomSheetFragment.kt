package com.example.pregnancytrackerignite.presentation.fragments.bottomSheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.StatsLog.logEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.LayoutRateUsBottomSheetBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.di.myApplication.MyApplication.Companion.isOutForRating
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RatingBottomSheetFragment : BottomSheetDialogFragment() {
    val binding by lazy {
        LayoutRateUsBottomSheetBinding.inflate(layoutInflater)
    }

    var name : ((String)->Unit)? = null
    var comment1State = false
    var comment2State = false
    var comment3State = false
    var comment4State = false
    var comment5State = false
    var starCount =  5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
         initListeners()

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
        }
    }
    private fun animateStarsToRating(targetRating: Int) {
        lifecycleScope.launch {
            for (i in 1..targetRating) {
                binding.simpleRatingBar.rating = i.toFloat()
                delay(200)
            }
        }
    }
    private fun initListeners() {

        binding.apply {
            lifecycleScope.launch {
                simpleRatingBar.rating =  1F
                delay(500)
                simpleRatingBar.rating =  2F
                delay(300)
                simpleRatingBar.rating =  3F
                delay(300)
                simpleRatingBar.rating =  4F
                delay(300)
                simpleRatingBar.rating =  5F
            }
            simpleRatingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    starCount = rating.toInt()
                    when(rating){
                        1F ->{
                            binding.emoji.setImageResource(R.drawable.emoji1)
                        }
                        2F ->{
                            binding.emoji.setImageResource(R.drawable.emoji2)
                        }
                        3F ->{
                            binding.emoji.setImageResource(R.drawable.emoji3)
                        }
                        4F ->{
                            binding.emoji.setImageResource(R.drawable.emoji4)
                        }
                        5F ->{
                        binding.emoji.setImageResource(R.drawable.emoji5)
                       }
                    }
                    animateStarsToRating(rating.toInt())
                }
            }

            binding.cross.setOnClickListener {
                dismiss()
            }

            comment1.setOnClickListener {
                if(comment1State){
                    comment1State = false
                    it.setBackgroundResource(R.drawable.stroke_btn)
                    binding.comment1Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.primary))
                }else{
                    comment1State = true
                    it.setBackgroundResource(R.drawable.item_ratting_filled)
                    binding.comment1Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }
            comment2.setOnClickListener {
                if(comment2State){
                    comment2State = false
                    it.setBackgroundResource(R.drawable.stroke_btn)
                    binding.comment2Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.primary))
                }else{
                    comment2State = true
                    it.setBackgroundResource(R.drawable.item_ratting_filled)
                    binding.comment2Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }
            comment3.setOnClickListener {
                if(comment3State){
                    comment3State = false
                    it.setBackgroundResource(R.drawable.stroke_btn)
                    binding.comment3Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.primary))
                }else{
                    comment3State = true
                    it.setBackgroundResource(R.drawable.item_ratting_filled)
                    binding.comment3Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }
            comment4.setOnClickListener {
                if(comment4State){
                    comment4State = false
                    it.setBackgroundResource(R.drawable.stroke_btn)
                    binding.comment4Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.primary))
                }else{
                    comment4State = true
                    it.setBackgroundResource(R.drawable.item_ratting_filled)
                    binding.comment4Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }
            comment5.setOnClickListener {
                if(comment5State){
                    comment5State = false
                    it.setBackgroundResource(R.drawable.stroke_btn)
                    binding.comment5Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.primary))
                }else{
                    comment5State = true
                    it.setBackgroundResource(R.drawable.item_ratting_filled)
                    binding.comment5Text.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }

            Start.setOnClickListener {
                if(starCount > 3){
                    MyApplication.mInstance.preferenceManager.put(PreferencesManager.Key.IS_NAVIGATE_FOR_RATTING,true)
                    dialog?.dismiss()
                    isOutForRating = true
                    val url =
                        "https://play.google.com/store/apps/details?id=com.babytracker.periodtracker.babygenderpredictor.pregnancyapp"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setData(Uri.parse(url))
                    startActivity(i)
                }else{
                    dismiss()
                }
            }
        }
    }
}

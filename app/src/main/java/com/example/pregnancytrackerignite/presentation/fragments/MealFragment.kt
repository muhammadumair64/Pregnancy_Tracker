package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentMealBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.popBackStack

class MealFragment : Fragment() {

    val binding by lazy {
        FragmentMealBinding.inflate(layoutInflater)
    }
    val viewModel:SharedViewModel by activityViewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    fun initViews(){
        viewModel.selectedMeal.let { model ->
            binding.apply {
                backBtn.setOnClickListener {
                    popBackStack()
                }
                prepTime.text = model?.prepTime
                cockTime.text = model?.cookTime
                mealName.text = model?.name
                val options: RequestOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.diet_placeholder)
                    .error(R.drawable.diet_placeholder)
                Glide.with(requireContext()).load(model?.imageUrl).apply(options).into(image)

                ingredients.text = model?.ingredients?.joinToString("\n") { "●  $it" }
                recipe.text = model?.recipe
                    ?.mapIndexed { index, item -> "${index + 1}. $item" }
                    ?.joinToString("\n \n")

            }
        }
    }

}
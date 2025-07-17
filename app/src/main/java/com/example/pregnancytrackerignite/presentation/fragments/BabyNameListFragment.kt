package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BabiesNameClass
import com.example.pregnancytrackerignite.databinding.FragmentBabyNameListBinding
import com.example.pregnancytrackerignite.presentation.adapters.BabyNameAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.BabiesNameViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.popBackStack
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import java.io.IOException
import java.io.InputStream

class BabyNameListFragment : Fragment() {

    val binding by lazy {
        FragmentBabyNameListBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()
    val viewModelNames : BabiesNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        fetchNameData("boy")
        return binding.root
    }

    private fun fetchNameData(type: String) {
        binding.apply {
            val country = viewModel.currentCountry
            // Convert List to MutableList
            val adapter = BabyNameAdapter() { id, isFavorite ->
                viewModelNames.updateFavoriteStatus(id, isFavorite)
            }
            if (binding.babiesRecyclerView.layoutManager == null) {
                binding.babiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
            binding.babiesRecyclerView.adapter = adapter

            // Use a one-time observer
            viewModelNames.fetchNames(type, country).observe(viewLifecycleOwner) { names ->
                if (!names.isNullOrEmpty()) {
                 adapter.updateList(names)
                } else {
                    Toast.makeText(requireContext(), "No names found", Toast.LENGTH_SHORT).show()
                }

                // Remove observer after first update
                viewModelNames.fetchNames(type, country).removeObservers(viewLifecycleOwner)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.apply {
            title.text = "${(viewModel.currentCountry)} Names"
            backBtn.setOnClickListener {
                popBackStack()
            }

            textBabyBoy.setOnClickListener{
                textBabyBoy.setBackgroundResource(R.drawable.shape_bg_fill)
                textBabyGirl.setBackgroundResource(R.drawable.shape_bg_white)
                textBabyBoy.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                textBabyGirl.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text_color))
                fetchNameData("boy")
            }
            textBabyGirl.setOnClickListener{
                textBabyBoy.setBackgroundResource(R.drawable.shape_bg_white)
                textBabyGirl.setBackgroundResource(R.drawable.shape_bg_fill)
                textBabyBoy.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text_color))
                textBabyGirl.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                fetchNameData("girl")
            }
        }
    }
}
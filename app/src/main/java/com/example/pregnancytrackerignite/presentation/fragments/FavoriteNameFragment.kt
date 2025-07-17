package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentCountriesBinding
import com.example.pregnancytrackerignite.databinding.FragmentFavoriteNameBinding
import com.example.pregnancytrackerignite.presentation.adapters.BabyNameAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.BabiesNameViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.NameNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast

class FavoriteNameFragment : Fragment() {

    val binding by lazy {
        FragmentFavoriteNameBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()
    val viewModelNames : BabiesNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        performNavigation()
        initViews()
        return binding.root
    }

    private fun performNavigation() {
        viewModel.nameCounterNavSelected = {
            when (it) {
                NameNavEvents.NavigateToFavorites -> {}
                NameNavEvents.NavigateToCountries -> {
                    safeNavigate(
                        R.id.action_favoriteNameFragment_to_countriesFragment,
                        R.id.favoriteNameFragment
                    )
                }
            }
        }
    }

    private fun initViews() {
        binding.apply {
            // Initialize the adapter outside the observer so it's not recreated every time data changes
            val babyNameAdapter = BabyNameAdapter { id, isFavorite ->
                viewModelNames.updateFavoriteStatus(id, isFavorite)
            }

            // Observe data from the ViewModel
            viewModelNames.getFavoriteNames().observe(viewLifecycleOwner) { favoriteNames ->
                if (!favoriteNames.isNullOrEmpty()) {
                    placeholder.visibility = View.GONE
                    babiesRecyclerView.visibility = View.VISIBLE

                    // Update the list in the adapter
                    babyNameAdapter.updateList(favoriteNames)

                    // Set the RecyclerView's layout manager and adapter only once
                    if (babiesRecyclerView.adapter == null) {
                        babiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        babiesRecyclerView.adapter = babyNameAdapter
                    }
                } else {
                    placeholder.visibility = View.VISIBLE
                    babiesRecyclerView.visibility = View.GONE
                }
            }
        }
    }

}
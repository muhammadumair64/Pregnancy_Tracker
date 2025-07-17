package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.CountryModel
import com.example.pregnancytrackerignite.databinding.FragmentCountriesBinding
import com.example.pregnancytrackerignite.databinding.FragmentKickCounterBinding
import com.example.pregnancytrackerignite.presentation.adapters.CountryAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.KickNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NameClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NameNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast

class CountriesFragment : Fragment() {

    val binding by lazy {
        FragmentCountriesBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        performNavigation()
        initRecyclerItems()
        initViews()
        return binding.root
    }

    private fun initRecyclerItems() {
        val countryList = listOf(
            CountryModel(1, R.drawable.pakistani_flag, "Pakistani"),
            CountryModel(2, R.drawable.arabic_flag, "Arabic"),
            CountryModel(3, R.drawable.flag_amarican, "American"),
            CountryModel(4, R.drawable.french_flag, "French"),
            CountryModel(5, R.drawable.indonasian_flag, "Indonesian"),
            CountryModel(5, R.drawable.flag_indian, "Indian"),
            CountryModel(6, R.drawable.portugese_flag, "Portuguese"),
            CountryModel(7, R.drawable.flag_canadian, "Canadian"),
            CountryModel(8, R.drawable.flag_german, "German"),
        ).sortedBy { it.name }

        val adapter = CountryAdapter(countryList) {
            viewModel.currentCountry = it.name
            viewModel.nameClickCallBack?.invoke(NameClickEvents.ClickOnCountryItem)
        }
        binding.countriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.countriesRecyclerView.adapter = adapter
    }

    private fun performNavigation() {
        viewModel.nameCounterNavSelected = {
            when (it) {
                NameNavEvents.NavigateToCountries -> {}
                NameNavEvents.NavigateToFavorites -> {
                    safeNavigate(
                        R.id.action_countriesFragment_to_favoriteNameFragment,
                        R.id.countriesFragment
                    )
                }
            }
        }
    }

    private fun initViews() {
        binding.apply {

        }
    }

}
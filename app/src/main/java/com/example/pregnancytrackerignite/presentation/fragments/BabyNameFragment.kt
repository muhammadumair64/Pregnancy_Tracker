package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BabiesNameClass
import com.example.pregnancytrackerignite.databinding.FragmentBabyNameBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.presentation.viewModel.BabiesNameViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.NameClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NameNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.invisible
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.visible
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.io.InputStream

class BabyNameFragment : Fragment() {

    val binding by lazy {
        FragmentBabyNameBinding.inflate(layoutInflater)
    }
    val viewModel : SharedViewModel by activityViewModels()
    val viewModelNames : BabiesNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        if (MyApplication.mInstance.preferenceManager.getBoolean(PreferencesManager.Key.IS_BABIES_NAME_ADDED, false)){
            Log.d("TAG", "onCreateView: alredy data in database")
        } else {
            MyApplication.mInstance.preferenceManager.put(PreferencesManager.Key.IS_BABIES_NAME_ADDED, true)
            initJsonNamesDataToDataBase()
        }
        return binding.root
    }

    private fun initJsonNamesDataToDataBase() {
        val namesData = getNamesData()
        viewModelNames.insertNames(namesData?.names ?: emptyList())
    }

    private fun initViews() {
        binding.apply {
            countriesTab.setOnClickListener { setTabBar(1) }
            favoriteTab.setOnClickListener { setTabBar(2) }
            backBtn.setOnClickListener { popBackStack() }
        }
        viewModel.nameClickCallBack = {
            when(it) {
                NameClickEvents.ClickOnCountryItem ->  safeNavigate(R.id.action_babyNameFragment_to_babyNameListFragment, R.id.babyNameFragment)
                NameClickEvents.ClickOnFavoriteItem -> {}
            }
        }
    }

    private fun setTabBar(selection: Int) {
        binding.apply {
            favoriteIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            countriesIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            favoriteText.setTextColor(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            countriesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.bar_light_color))
            footIndicator.invisible()
            recordIndicator.invisible()

            when (selection) {
                1 -> {
                    viewModel.nameCounterNavSelected?.invoke(NameNavEvents.NavigateToCountries)
                    countriesIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary))
                    countriesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    footIndicator.visible()
                }
                2 -> {
                    viewModel.nameCounterNavSelected?.invoke(NameNavEvents.NavigateToFavorites)
                    favoriteIc.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary))
                    favoriteText.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    recordIndicator.visible()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handleBackPress {
            popBackStack()
        }
    }

    private fun getNamesData(): BabiesNameClass? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val json = getJsonFromAssets(requireContext(), "babies_name.json")
        Log.d(tag, "FILE IS $json")

        return try {
            val jsonAdapter = moshi.adapter(BabiesNameClass::class.java)
            jsonAdapter.fromJson(json!!)
        } catch (e: JsonDataException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
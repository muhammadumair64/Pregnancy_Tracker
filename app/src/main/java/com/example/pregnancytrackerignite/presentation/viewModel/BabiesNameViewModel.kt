package com.example.pregnancytrackerignite.presentation.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.BabiesNameModel
import com.example.pregnancytrackerignite.data.repositories.BabiesNameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabiesNameViewModel @Inject constructor(private val babiesNameRepository: BabiesNameRepository) : ViewModel() {

    fun insertNames(names: List<BabiesNameModel>) = viewModelScope.launch {
        babiesNameRepository.insertNames(names)
    }

    fun fetchNames(type: String, country: String): LiveData<List<BabiesNameModel>> {
        return babiesNameRepository.fetchNames(type, country)
    }

    fun getFavoriteNames(): LiveData<List<BabiesNameModel>> {
        return babiesNameRepository.getFavoriteNames()
    }

    fun updateFavoriteStatus(id: Int, isFavorite: Boolean) = viewModelScope.launch {
        babiesNameRepository.updateFavoriteStatus(id, isFavorite)
    }
}
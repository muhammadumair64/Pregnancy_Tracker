package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.repositories.BloodPressureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodPressureViewModel @Inject constructor(private val bpRepo: BloodPressureRepository,): ViewModel() {

    var latestBpModel = bpRepo.latestValues
    var allBpLiveData = bpRepo.getAllBpModels()

    fun addBpModel(bpModel: BpModel) {
        viewModelScope.launch {
            bpRepo.insertBp(bpModel)
        }
    }
}
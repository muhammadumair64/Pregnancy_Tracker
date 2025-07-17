package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.repositories.KickRecordRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KickRecordViewModel @Inject constructor(private val recordRepo: KickRecordRepo): ViewModel() {

    var latestReportModel = recordRepo.latestValues
    var allReportLiveData = recordRepo.getAllKickModels()

    fun addReportModel(recordModel: KickDataClass) {
        viewModelScope.launch {
            recordRepo.insertKick(recordModel)
        }
    }

    fun deleteReportModel(recordModel: KickDataClass) {
        viewModelScope.launch {
            recordRepo.deleteKick(recordModel)
        }
    }
}
package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.ScanDataClass
import com.example.pregnancytrackerignite.data.repositories.ScansRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanRepo: ScansRepo,): ViewModel() {

    var latestScanModel = scanRepo.latestValues
    var allScanLiveData = scanRepo.getAllScanModels()

    fun addScanModel(scanModel: ScanDataClass) {
        viewModelScope.launch {
            scanRepo.insertScan(scanModel)
        }
    }

    fun deleteScanModel(scanModel: ScanDataClass) {
        viewModelScope.launch {
        scanRepo.deleteScan(scanModel)
        }
    }
}
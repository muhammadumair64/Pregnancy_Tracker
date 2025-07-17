package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.repositories.BloodPressureRepository
import com.example.pregnancytrackerignite.data.repositories.ReportsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val reportRepo: ReportsRepo,): ViewModel() {

    var latestReportModel = reportRepo.latestValues
    var allReportLiveData = reportRepo.getAllReportModels()

    fun addReportModel(reportModel: ReportDataClass) {
        viewModelScope.launch {
            reportRepo.insertReport(reportModel)
        }
    }

    fun deleteReportModel(reportModel: ReportDataClass) {
        viewModelScope.launch {
        reportRepo.deleteReport(reportModel)
        }
    }
}
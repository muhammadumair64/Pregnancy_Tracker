package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.GestationalAge
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import com.example.pregnancytrackerignite.data.repositories.PregnancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class PregnancyViewModel @Inject constructor(private val repository: PregnancyRepository) :
    ViewModel() {
    fun addOrUpdatePregnancyData(data: PregnancyPeriod) {
        viewModelScope.launch {
            val period = when {
                data.lastPeriodDate != null -> {
                    val gestationalAge = calculateGestationalAgeFromDate(data.lastPeriodDate)
                    val estimatedDate = calculateEstimatedDate(data.lastPeriodDate)
                    PregnancyPeriod(data.lastPeriodDate, gestationalAge, estimatedDate)
                }

                data.gestationalAge != null -> {
                    val lastPeriodDate = mcalculateLastMenstrualPeriodMillis(data.gestationalAge)
                    val estimatedDate = calculateEstimatedDate(lastPeriodDate)
                    PregnancyPeriod(lastPeriodDate, data.gestationalAge, estimatedDate)
                }

                data.estimatedDate != null -> {
                    val lastPeriodDate = calculateLastMenstrualPeriodMillis(data.estimatedDate)
                    val gestationalAge = calculateGestationalAgeFromDate(lastPeriodDate)
                    PregnancyPeriod(lastPeriodDate, gestationalAge, data.estimatedDate)
                }

                else -> null
            }
            period?.let {
                repository.insertOrUpdatePregnancyData(it)
            }
        }
    }

    private fun calculateGestationalAgeFromDate(lastPeriodDate: Long): GestationalAge {
        val currentDate = System.currentTimeMillis()
        val diffInMilliseconds = abs(currentDate - lastPeriodDate)
        val diffInDays = diffInMilliseconds / (1000 * 60 * 60 * 24)
        val weeks = (diffInDays / 7).toInt()
        val days = (diffInDays % 7).toInt()
        return GestationalAge(weeks, days)
    }

    private fun calculateEstimatedDate(lastPeriodDate: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = lastPeriodDate
        calendar.add(Calendar.DAY_OF_YEAR, 280) // Adding 280 days for an average pregnancy
        return calendar.timeInMillis
    }

    private fun calculateLastMenstrualPeriodMillis(estimatedDueDate: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = estimatedDueDate
        calendar.add(Calendar.WEEK_OF_YEAR, -40) // Subtracting 40 weeks to estimate LMP
        return calendar.timeInMillis
    }

    private fun mcalculateLastMenstrualPeriodMillis(gestationalAge: GestationalAge): Long {
         val currentMillis = System.currentTimeMillis()
         val gestationalDaysMillis = (gestationalAge.weeks * 7 + gestationalAge.days) * 24 * 60 * 60 * 1000L
        return currentMillis - gestationalDaysMillis
    }
}

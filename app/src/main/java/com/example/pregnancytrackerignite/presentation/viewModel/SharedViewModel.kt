package com.example.pregnancytrackerignite.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.CombinedData
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.models.DashboardArticles
import com.example.pregnancytrackerignite.data.models.ExerciseType
import com.example.pregnancytrackerignite.data.models.GenderPredictionResult
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.repositories.DashboardArticlesRepo
import com.example.pregnancytrackerignite.data.repositories.GenderRepository
import com.example.pregnancytrackerignite.data.repositories.PregnancyRepository
import com.example.pregnancytrackerignite.data.utils.Meal
import com.example.pregnancytrackerignite.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.abs
enum class NavEvents{
    NavigateToHome,
    NavigateToDiet,
    NavigateToTools,
    NavigateToExercise
}

enum class KickNavEvents {
    NavigateToCounter, NavigateToRecords
}

enum class NameNavEvents {
    NavigateToCountries, NavigateToFavorites
}

enum class NameClickEvents {
    ClickOnCountryItem, ClickOnFavoriteItem
}


enum class ClickEvents{
    ClickOnPrediction,
    ClickOnBP,
    ClickExercise,
    ClickBlog,
    ClickOnSettings,
    ClickOnScans,
    ClickOnDueDate,
    ClickOnBabySize,
    ClickOnReport,
    ClickOnLungsExercise,
    ClickOnKickCounter,
    ClickOnOvulation,
    ClickOnWeightTracker,
    ClickOnMedicineReminder,ClickAiDoctor,ClickOnMeal,ClickOnViewAll, ClickOnBabyNames , ClickOnTrimester , ClickOnMainImage
}

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pregnancyRepository: PregnancyRepository,
    private val dashboardArticlesRepo: DashboardArticlesRepo,
    private val genderRepository: GenderRepository
) : ViewModel() {
    var currentUser = MutableStateFlow<CurrentUser?>(null)
        private set
    var genderResult = MutableStateFlow<GenderPredictionResult?>(null)
        private set
    var pregnancyData = MutableStateFlow<PregnancyPeriod?>(null)
        private set

    var _navEvents = Channel<NavEvents>()
    var selectedFile : ReportDataClass? = null
    var selectedReminder : MedicineReminderDataClass? = null
    var selectedMeal : Meal? = null
    var isFromReminderRV = false
    var currentWeek = ""
    var preWeight = ""
    var currentWeight = ""
    var height = ""
    var lastPeriodDate = ""
    var periodCycleDays = ""
    var currentCountry = ""
    var weeks = 0
    var isFromSettings = false

    var vBlogsList: ArrayList<MainBlogModel> = ArrayList()
    val navEvents = _navEvents.receiveAsFlow()

    var tipCounter = 0

    private var remainingDaysInDelivery = MutableStateFlow("...")
    private var weightOfBaby = MutableStateFlow("...")
    private var heightOfBaby = MutableStateFlow("...")
    private var trimster = MutableStateFlow(0)
        private set
    val listOfDashboardArticles = dashboardArticlesRepo.listOfItems
    private var clickedArticle = MutableLiveData<DashboardArticles?>()
        private set

    // Navigation
    var onNavItemSelected: ((NavEvents) -> Unit)? = null
    var handleNavUIStates: ((NavEvents) -> Unit)? = null
    var kickCounterNavSelected: ((KickNavEvents) -> Unit)? = null
    var nameCounterNavSelected: ((NameNavEvents) -> Unit)? = null
    var nameClickCallBack: ((NameClickEvents) -> Unit)? = null
    var clickCallBack: ((ClickEvents) -> Unit)? = null
    var selectedExerciseItem: ExerciseType? = null
    var selectedBlogItem: MainBlogModel? = null

    private val _visitCount = MutableLiveData(0)
    val visitCount: LiveData<Int> get() = _visitCount

    fun incrementVisitCount() {
        _visitCount.value = (_visitCount.value ?: 0) + 1
    }


    init {
        getUser()
        getPregnancyData()
        getGender()
        getArticles()
    }

    fun setClickedArticle(article: DashboardArticles) {
        viewModelScope.launch {
            clickedArticle.value = (article)
        }
    }

    private fun getArticles() {
        viewModelScope.launch {
            dashboardArticlesRepo.getArticles()
        }
    }

    val combinedData = combine(
        currentUser, pregnancyData, remainingDaysInDelivery, weightOfBaby, heightOfBaby
    ) { user, data, days, weight, height ->
        user?.let {
            data?.let { data ->

                CombinedData(
                    it, data, days, weight, height, trismsterPeriod = null
                )
            }
        }
    }

    private fun getPregnancyData() {
        viewModelScope.launch {
            pregnancyRepository.getCurrentUser().flowOn(Dispatchers.IO).collect { data ->
                pregnancyData.update {
                    data
                }
                data?.estimatedDate?.let { calculateDaysRemaining(it) }
                data?.gestationalAge?.let {
                    estimateBabyWeightForWeek(it.weeks)
                    estimateBabyHeightForWeek(it.weeks)
                    getCurrentTrimester(it.weeks)
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().flowOn(Dispatchers.IO).collect { user ->
                user?.let {
                    currentUser.update {
                        user
                    }
                }
            }
        }
    }

    private fun getGender() {
        viewModelScope.launch {
            genderRepository.getPredictionResult().flowOn(Dispatchers.IO).collect { result ->
                result?.let {
                    genderResult.update {
                        result
                    }
                }
            }
        }
    }

    fun setGenderData(predictionResult: GenderPredictionResult) {
        viewModelScope.launch {
            genderRepository.insertGenderPrediction(predictionResult)
        }
    }


    private fun calculateDaysRemaining(targetDateMillis: Long) {
        val currentDate = LocalDate.now()
        val targetDate = LocalDate.ofEpochDay(targetDateMillis / (24 * 60 * 60 * 1000))
        val days = ChronoUnit.DAYS.between(currentDate, targetDate)
        remainingDaysInDelivery.update {
            "$days days"
        }
    }

    private fun estimateBabyWeightForWeek(week: Int) {
        weightOfBaby.update {
            when (week) {
                in 8..40 -> calculateWeightForWeek(week)
                else -> "0 gm"
            }
        }
    }

    private fun estimateBabyHeightForWeek(week: Int) {
        heightOfBaby.update {
            when (week) {
                in 8..20 -> calculateHeightForWeek(week)
                in 21..40 -> "%.1f cm".format((16.4f + (week - 20) * 0.1f) * 2.54) // Linear increase after 20 weeks
                else -> "Negligible"
            }
        }
    }

    fun calculateWeightForWeek(week: Int): String {
        return when (week) {
            8 -> "1 gm"
            9 -> "2 gm"
            10 -> "4 gm"
            11 -> "7 gm"
            12 -> "14 gm"
            13 -> "23 gm"
            14 -> "43 gm"
            15 -> "70 gm"
            16 -> "100 gm"
            17 -> "140 gm"
            18 -> "190 gm"
            19 -> "240 gm"
            20 -> "300 gm"
            21 -> "360 gm"
            22 -> "430 gm"
            23 -> "501 gm"
            24 -> "600 gm"
            25 -> "660 gm"
            26 -> "760 gm"
            27 -> "875 gm"
            28 -> "1005 gm"
            29 -> "1153 gm"
            30 -> "1319 gm"
            31 -> "1502 gm"
            32 -> "1702 gm"
            33 -> "1918 gm"
            34 -> "2146 gm"
            35 -> "2383 gm"
            36 -> "2622 gm"
            37 -> "2859 gm"
            38 -> "2859 gm"
            39 -> "3083 gm"
            40 -> "3288 gm"
            else -> "Negligible"
        }
    }

    fun calculateHeightForWeek(week: Int): String {
        return when (week) {
            8 -> "%.1f cm".format(1.6 * 2.54)
            9 -> "%.1f cm".format(2.3 * 2.54)
            10 -> "%.1f cm".format(3.1 * 2.54)
            11 -> "%.1f cm".format(4.1 * 2.54)
            12 -> "%.1f cm".format(5.4 * 2.54)
            13 -> "%.1f cm".format(7.4 * 2.54)
            14 -> "%.1f cm".format(8.7 * 2.54)
            15 -> "%.1f cm".format(10.1 * 2.54)
            16 -> "%.1f cm".format(11.6 * 2.54)
            17 -> "%.1f cm".format(13.0 * 2.54)
            18 -> "%.1f cm".format(14.2 * 2.54)
            19 -> "%.1f cm".format(15.3 * 2.54)
            20 -> "%.1f cm".format(16.4 * 2.54)
            in 21..40 -> {
                val height = when (week) {
                    in 21..23 -> (week - 20) * 0.3 + 10.5
                    in 24..26 -> (week - 20) * 0.5 + 11.0
                    in 27..29 -> (week - 20) * 0.7 + 11.4
                    in 30..32 -> (week - 20) * 0.8 + 12.0
                    in 33..35 -> (week - 20) * 0.8 + 12.6
                    else -> (week - 20) * 0.4 + 12.6
                }
                return "%.1f cm".format(height)
            }

            else -> "0 cm"
        }
    }

    private fun getCurrentTrimester(week: Int) {
        val trimester: Int = when (week) {
            in 1..12 -> {
                1
            }

            in 13..26 -> {
                2
            }

            in 27..40 -> {
                3
            }

            else -> {
                0
            }
        }
        trimster.update {
            trimester
        }
    }

    fun calculateWeeksForImage(lastPeriodDate: Long, currentDate: Long): Int {
        val diffInMilliseconds = abs(currentDate - lastPeriodDate)
        val diffInDays = diffInMilliseconds / (1000 * 60 * 60 * 24)
        Log.d("SharedViewModel", "calculateWeeksForImage: $diffInDays")
        val weeks = (diffInDays / 7).toInt()
        val days = (diffInDays % 7).toInt()
        Log.d("SharedViewModel", "calculateWeeksForImage: $days")
        return weeks
    }

    fun calculateNextDateMillis(daysToAdd: Int, dayToMinus: Int): Long {
        // Get the current date and time
        val currentDateTime = System.currentTimeMillis()
        var nextDateTime: Long = 0

        if (dayToMinus != 0) {
            nextDateTime = currentDateTime + (86400000 * daysToAdd)
        } else {
            nextDateTime = currentDateTime - (86400000 * daysToAdd)
        }

        return nextDateTime
    }

    fun calculateWeekDifference(
        startDay: Int, startMonth: Int, startYear: Int, endDay: Int, endMonth: Int, endYear: Int
    ): Long {
        // Create LocalDate instances for the start and end dates
        val startDate = LocalDate.of(startYear, startMonth, startDay)
        val endDate = LocalDate.of(endYear, endMonth, endDay)

        // Calculate the difference in weeks between the two dates
        return ChronoUnit.WEEKS.between(startDate, endDate)
    }
}

package com.example.pregnancytrackerignite.presentation.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.DateModel
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.data.models.MainBlogsClass
import com.example.pregnancytrackerignite.data.utils.AdsCounter
import com.example.pregnancytrackerignite.data.utils.Constants
import com.example.pregnancytrackerignite.databinding.FragmentHomeBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.example.pregnancytrackerignite.presentation.adapters.BlogsListHorizontalAdapter
import com.example.pregnancytrackerignite.presentation.fragments.bottomSheet.RatingBottomSheetFragment
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.rubik_cube_solver.data.utils.toFormattedDateString
import com.iobits.videocompressor.utils.formatToCustomString
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.handleLastBackPress
import com.iobits.videocompressor.utils.invisible
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.visible
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.Calendar

class HomeFragment : Fragment() {

    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var mBlogsList: ArrayList<MainBlogModel> = ArrayList()
    private var currentDate: DateModel? = null
    private var currentWeek = 0
    private val TAG = "HomeFragmentTag"
    private var backPressedTime: Long = 0
    private var backToast: Toast? = null
    private val viewModel: SharedViewModel by activityViewModels()
    private val viewModelBloodPressure: BloodPressureViewModel by activityViewModels()

    private var gender = ""
    private var solvedQuestions = 0
    private var totalQuestions = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.tipCounter ++
        performNavigation()
        initListeners()
        initViews()
        genderPrediction()
        setGreeting()
        setBloodPressureDate()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        handleLastBackPress {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast?.cancel()
                requireActivity().moveTaskToBack(false)
            } else {
                backToast = Toast.makeText(
                    requireContext(),
                    getString(R.string.press_back_again_to_exit),
                    Toast.LENGTH_SHORT
                )
                backToast?.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    @SuppressLint("LogNotTimber")
    private fun initListeners() {

        lifecycleScope.launch {
            delay(500)
            binding.calendar.setTodaySelected()
        }

        binding.apply {
            circularImageView.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnMainImage)
            }

            settings.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnSettings)
            }

            aiDoctor.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickAiDoctor)
            }

            aiDoctorCard.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickAiDoctor)
            }

            calendar.setOnDateSelectListener { dateModel ->
                if (currentDate == null) {
                    currentDate = dateModel
                } else {
                    try {
                        val myWeeks = currentDate?.let {
                            viewModel.calculateWeekDifference(
                                it.day,
                                it.monthNumber,
                                it.year,
                                dateModel.day,
                                dateModel.monthNumber,
                                dateModel.year
                            )
                        }
                        if (myWeeks != null) {
                            val index = (currentWeek + myWeeks) - 1
                            if (index.toInt() >= 0) {
                                setDataOnManuallyDayChange(myWeeks, index)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "initListeners: ERROR ${e.localizedMessage}")
                    }
                }
            }

            forwardArrow.setOnClickListener {
                calendar.setForward()
            }

            backwardArrow.setOnClickListener {
                calendar.setBackward()
            }

            bloodPressureCard.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnBP)
            }

            dueDateCard.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnDueDate)
            }
        }
    }

    private fun setDataOnManuallyDayChange(myWeeks: Long, index: Long) {
        viewModel.listOfDashboardArticles.observe(viewLifecycleOwner) {
            if (it.size >= index) {
                binding.circularImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), it[index.toInt()].image
                    )
                )
            }
        }
        binding.apply {
            progressCircularId.progress = (currentWeek + myWeeks).toFloat() ?: 0f
            progressTrimster.setProgress(
                (((currentWeek + myWeeks) ?: 0f).toInt())
            )
            binding.weeksOfPragnency.text = "Week ${(currentWeek + myWeeks)}"
            weightInGramTxt.text = viewModel.calculateWeightForWeek((currentWeek + myWeeks).toInt())
            heightInCm.text = viewModel.calculateHeightForWeek((currentWeek + myWeeks).toInt())
            trimesterChartSetup((currentWeek + myWeeks).toInt())
        }
    }

    private fun initViews() {
        setBlogs()
        rotateImageView()
        showRatingDialog()
        binding.apply {
             size.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnBabySize)
             }
            weight.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnBabySize)
            }
            days.setOnClickListener {
            // mainScroll.smoothScrollTo(0,binding.genderCard.top)
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnTrimester)
            }
            trimesterChart.root.setOnClickListener {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnTrimester)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.combinedData.collect { combinedData ->
                    try {
                        binding.apply {
                            logd(combinedData?.user.toString(), "dashboard")
                            profileName.text = "${combinedData?.user?.name}"
                            currentWeek = combinedData?.data?.gestationalAge?.weeks ?: 0
                            weeksOfPragnency.text = "Week ${combinedData?.data?.gestationalAge?.weeks}"
                            viewModel.currentWeek = "Week ${combinedData?.data?.gestationalAge?.weeks}"
                            viewModel.weeks = combinedData?.data?.gestationalAge?.weeks ?: 0
                            trimesterChartSetup(combinedData?.data?.gestationalAge?.weeks ?: 1)
                            tvDueDate.text = combinedData?.data?.estimatedDate?.toFormattedDateString()
                            weightInGramTxt.text = combinedData?.weight
                            heightInCm.text = combinedData?.heightRecord
                            daysText.text = "Day ${combinedData?.data?.gestationalAge?.days}"
                            val indexForImg = if (combinedData?.data?.gestationalAge?.weeks!! > 0) combinedData.data.gestationalAge.weeks.minus(
                                1
                            ) else 0
                            logd("$indexForImg")

                            viewModel.listOfDashboardArticles.observe(viewLifecycleOwner) {
                                if (it.size >= indexForImg) {
                                    binding.circularImageView.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            requireContext(), it[indexForImg].image
                                        )
                                    )
                                }
                            }

                            when (Constants.getCurrentTrimester(
                                combinedData.data.gestationalAge.weeks ?: 0
                            )) {
                                1 -> {
                                    trimesterSet.text = "First Trimester"
                                }

                                2 -> {
                                    trimesterSet.text = "Second Trimester"
                                }

                                3 -> {
                                    trimesterSet.text = "Third Trimester"
                                }
                            }
                            delay(500)
                            progressTrimster.setProgress(
                                combinedData.data.gestationalAge.weeks ?: 0
                            )
                            progressCircularId.progress =
                                combinedData.data.gestationalAge.weeks.toFloat() ?: 0f
                        }
                    }catch (e:Exception){e.localizedMessage}
                }
            }
        }
    }

    private fun showRatingDialog() {
        if(AdsCounter.isShowRatting()){
            if(!MyApplication.mInstance.preferenceManager.getBoolean(PreferencesManager.Key.IS_NAVIGATE_FOR_RATTING)){
                val bottomSheetFragment = RatingBottomSheetFragment()
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
               }
            }
        }


    private fun rotateImageView(){
        val rotationAnimator = ObjectAnimator.ofFloat(binding.circularImageView, "rotation", 0f, 360f)
        rotationAnimator.apply {
            duration = 100000
            repeatCount = ObjectAnimator.INFINITE  // Repeat infinitely
            repeatMode = ObjectAnimator.RESTART  // Restart the animation after each cycle
            interpolator = LinearInterpolator()  // Make the rotation smooth and linear
            start()  // Start the animation
        }
    }

    private fun genderPrediction() {
        lifecycleScope.launch {
            viewModel.genderResult.collect { result ->
                gender = result?.gender.toString()
                solvedQuestions = result?.solvedQuestion ?: 0
                val temp = result?.totalQuestions ?: 20
                totalQuestions = if (temp != 0) {
                    temp
                } else {
                    20
                }
                binding.apply {
                    if(solvedQuestions == 0){
                        predictionText.text = "Baby --"
                        val text = "You haven’t answered any questions yet. Start now to get accurate results!"
                        binding.predictionDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            Html.fromHtml(text)
                        }
                    } else {
                        predictionText.text = "Baby $gender"
                        if (gender == "Boy") {
                            setFormattedText(totalQuestions,solvedQuestions,binding.predictionDescription, true)
                        } else {
                            setFormattedText(totalQuestions,solvedQuestions,binding.predictionDescription, false)
                        }
                    }
                    progressPrediction.progress = solvedQuestions.toFloat()
                    val mDiv = (solvedQuestions.div(totalQuestions.toFloat())).toFloat()
                    progressText.text = "${(mDiv * 100).toInt()}%"
                    if( (mDiv * 100).toInt() == 100){
                        binding.completeQuiz.invisible()
                    }
                }
            }
        }

        binding.genderCard.setOnClickListener {
            if (solvedQuestions == totalQuestions) {
                Toast.makeText(requireContext(), "Well done, Quiz Complete", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.clickCallBack?.invoke(ClickEvents.ClickOnPrediction)
            }
        }
    }

    private fun setFormattedText(
        total: Int,
        solvedQuestion: Int,
        textView: TextView,
        isBoy: Boolean
    ) {
        var text = ""
        if(solvedQuestions == total){
            text = if (isBoy) {
                "You’ve answered all questions, and <b>it’s a boy!</b> Congratulations on completing the prediction!"
            } else {
                "You’ve answered all questions, and <b>it’s a girl!</b> Congratulations on completing the prediction!"
            }
        } else {
             text = if (isBoy) {
                "You’ve answered $solvedQuestion questions, and <b>it’s a boy!</b> Complete more questions for accurate results."
            } else {
                "You’ve answered $solvedQuestion questions, and <b>it’s a girl!</b> Complete more questions for accurate results."
            }
        }

        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }

    private fun performNavigation() {
        viewModel.incrementVisitCount()
        viewModel.handleNavUIStates?.invoke(NavEvents.NavigateToHome)

        viewModel.onNavItemSelected = {
            when (it) {
                NavEvents.NavigateToHome -> {}
                NavEvents.NavigateToDiet -> { safeNavigate(R.id.action_homeFragment2_to_dietPlanFragment, R.id.homeFragment2) }
                NavEvents.NavigateToTools -> { safeNavigate(R.id.action_homeFragment2_to_toolsFragment, R.id.homeFragment2) }
                NavEvents.NavigateToExercise -> { safeNavigate(R.id.action_homeFragment2_to_exerciseFragment, R.id.homeFragment2) }
            }
        }
    }

    private fun setGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        binding.greetingText.text = greeting
    }

    private fun setBloodPressureDate() {
        viewModelBloodPressure.latestBpModel.observe(viewLifecycleOwner) {
            try {
                binding.systolicValue.text = it.systolic.toString() + "mmhg"
                binding.diastolicValue.text = it.diastolic.toString() + "mmhg"
                binding.pulseValue.text = it.pulse.toString() + "bpm"
                binding.bloodPressureDate.text = it.date.formatToCustomString()
            } catch (e: Exception) {
                logd("initData: LAST_BLOOD_PRESSURE ${e.localizedMessage}")
            }
        }
    }

    private fun setBlogs() {
        binding.viewallText.setOnClickListener {
            viewModel.clickCallBack?.invoke(ClickEvents.ClickOnViewAll)
        }

        lifecycleScope.launch {
            val blogsData = getBlogsData()
            mBlogsList = blogsData?.blogs as ArrayList<MainBlogModel>
            viewModel.vBlogsList = mBlogsList
            mBlogsList.shuffle()
            // Set up the horizontal RecyclerView with the filtered list
            binding.horizontalRv.adapter = BlogsListHorizontalAdapter(requireContext(), mBlogsList) { blogsData ->
                viewModel.selectedBlogItem = blogsData
                viewModel.clickCallBack?.invoke(ClickEvents.ClickBlog)
            }
            binding.horizontalRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun getBlogsData(): MainBlogsClass? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val json = getJsonFromAssets(requireContext(), "main_blogs_recycler.json")
        Log.d(tag, "FILE IS $json")

        return try {
            val jsonAdapter = moshi.adapter(MainBlogsClass::class.java)
            jsonAdapter.fromJson(json)
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
        }  catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun trimesterChartSetup(week: Int) {
        val selectors = binding.trimesterChart.run {
            listOf(
                selector0, selector1, selector2, selector3, selector4, selector5, selector6, selector7,
                selector8, selector9, selector10, selector11, selector12, selector13, selector14,
                selector15, selector16, selector17, selector18, selector19, selector20, selector21,
                selector22, selector23, selector24, selector25, selector26, selector27, selector28,
                selector29, selector30, selector31, selector32, selector33, selector34, selector35,
                selector36, selector37, selector38, selector39, selector40
            )
        }
        selectors.forEach { it.gone() }
        if (week in 0..40) {
            selectors[week].visible()
        }
    }
}

package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.pregnancytrackerignite.data.utils.CustomSnapHelper
import com.example.pregnancytrackerignite.data.utils.SizeHelper
import com.example.pregnancytrackerignite.databinding.FragmentBabySizeBinding
import com.example.pregnancytrackerignite.presentation.adapters.SizeCarouselAdapter
import com.example.pregnancytrackerignite.presentation.adapters.WeekRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.popBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class BabySizeFragment : Fragment() {
    val binding by lazy {
        FragmentBabySizeBinding.inflate(layoutInflater)
    }
    val snapHelper = CustomSnapHelper()
   lateinit var mLayoutManager :LinearLayoutManager
    private var week = 0
    private var mList: ArrayList<String> = ArrayList()
    private val viewModelForData: SharedViewModel by activityViewModels()
    private var weekAdapter: WeekRvAdapter? = null
    private val TAG = "BabySizeFragmentTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            initViews()
            setUpBottomInfo()
        } catch (e: Exception) {
            e.localizedMessage
        }
        return binding.root
    }

    fun initViews() {
        lifecycleScope.launch {
            SizeHelper.addSizeInList(requireContext())
        }
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelForData.combinedData.collect { combinedData ->
                    try {
                        week = combinedData?.data?.gestationalAge?.weeks ?: 0
                        binding.apply {
                            binding.weekRv.apply {
                                binding.weekRv.layoutManager = mLayoutManager
                                week++
                                var selectedPosition = 0
                                if (week > 0) {
                                    selectedPosition = week - 3
                                    if(selectedPosition < 0){
                                        selectedPosition = 0
                                        viewPager.setCurrentItem(selectedPosition, true)  // Set viewpager without animation for initial sync
                                        binding.fruitName.text = SizeHelper.fruitNameList[selectedPosition]
                                    }
                                }

                                weekAdapter = WeekRvAdapter(requireContext(), mList, selectedPosition)
                                adapter = weekAdapter

                                // Attach SnapHelper for center alignment
                                if (this.onFlingListener == null) {
                                    snapHelper.attachToRecyclerView(this)
                                }

                                // Smooth scroll to initial position
                                delay(1000)
                                smoothScrollToPosition(week - 3)

                                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                                            val centerView = snapHelper.findSnapView(mLayoutManager) ?: return
                                            val position = mLayoutManager!!.getPosition(centerView)
                                            weekAdapter?.updateSelectedPosition(position)
                                            // Scroll to ensure the first or last item can center properly
                                            if (position == 0 || position == weekAdapter!!.itemCount - 1) {
                                                smoothScrollToPosition(position)
                                            }
                                            setDataOnManuallyDayChange(position + 1)
                                            viewPager.setCurrentItem(position, false)
                                        }
                                    }
                                })
                            }
                        }
                    }catch (e:Exception){e.localizedMessage}
                }
            }
        }

        binding.backBtn.setOnClickListener {
            popBackStack()
        }

        mList.apply {
            for (i in 4..40) {
                add("$i")
            }
        }

        setUpViewPager(binding.viewPager)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDataOnManuallyDayChange(myWeeks: Int) {
        try {
//            weekAdapter?.selected = myWeeks - 1
//            weekAdapter?.notifyDataSetChanged()

            binding.apply {
                weeksOfPragnency.text = "Week ${(myWeeks + 3)}"
                weightInGramTxt.text = viewModelForData.calculateWeightForWeek((myWeeks + 3).toInt())
                heightInCm.text = viewModelForData.calculateHeightForWeek((myWeeks + 3).toInt())
            }

            if (myWeeks != 0) {
                binding.fruitName.text = SizeHelper.fruitNameList[myWeeks - 1]
            }
            //binding.weekRv.smoothScrollToPosition(myWeeks)
        } catch (e: Exception) {
            Log.d(TAG, "setDataOnManuallyDayChange: ${e.localizedMessage}")
        }
    }


    private fun setUpViewPager(viewPager: ViewPager2) {
        val imageCarouselAdapter = SizeCarouselAdapter(SizeHelper.sizeList)
        binding.viewPager.adapter = imageCarouselAdapter

        binding.apply {
            viewPager.offscreenPageLimit = 1
            viewPager.isUserInputEnabled = true
            viewPager.clipToPadding = false
            viewPager.clipChildren = false
            viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            viewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    viewPager.setCurrentItem(0, false)  // Disable animation on layout
                    val childView = viewPager.getChildAt(0)
                    childView?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }
            })

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(8))
            compositePageTransformer.addTransformer { page, position ->
                val absPosition = abs(position)
                val scaleFactor = 0.75f + (1 - absPosition) * 0.15f
                page.scaleY = scaleFactor
                page.scaleX = scaleFactor
                val alphaFactor = 0.7f + (1 - absPosition) * 0.3f
                page.alpha = alphaFactor
                page.translationX = -position * page.width * 0.1f
            }
            viewPager.setPageTransformer(compositePageTransformer)
        }

        // Sync RecyclerView when ViewPager position changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var isUserScroll = false

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (isUserScroll) {
                    if(position == 0){
                        binding.weekRv.smoothScrollToPosition(position)
                    }else{
                        weekAdapter?.updateSelectedPosition(position)

                        // Find the exact position to center the item
                        val itemView = binding.weekRv.layoutManager?.findViewByPosition(position)
                        itemView?.let {
                            val recyclerViewWidth = binding.weekRv.width
                            val itemWidth = it.width
                            val itemLeft = it.left
                            val centerOffset = (recyclerViewWidth / 2) - (itemWidth / 2)

                            // Smooth scroll to center the item
                            binding.weekRv.smoothScrollBy(itemLeft - centerOffset, 0)
                        }

                        setDataOnManuallyDayChange(position + 1)
                    }

                }

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> isUserScroll = true
                    ViewPager2.SCROLL_STATE_IDLE -> isUserScroll = false
                    ViewPager2.SCROLL_STATE_SETTLING -> {}
                }
            }
        })
    }

    private fun setUpBottomInfo() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelForData.combinedData.collect { combinedData ->
                    binding.apply {
                        weeksOfPragnency.text = "Week ${combinedData?.data?.gestationalAge?.weeks}"
                        weightInGramTxt.text = combinedData?.weight
                        heightInCm.text = combinedData?.heightRecord
                    }
                }
            }
        }
    }
}

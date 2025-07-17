package com.example.pregnancytrackerignite.presentation.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.DateModel
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.databinding.FragmentMedicineReminderBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import com.example.pregnancytrackerignite.presentation.adapters.MedicineItemRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.MedicineViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class MedicineReminderFragment : Fragment() {

    val binding by lazy {
        FragmentMedicineReminderBinding.inflate(layoutInflater)
    }

    private var mList = ArrayList<MedicineReminderDataClass>()
    private var mSortedList = ArrayList<MedicineReminderDataClass>()
    private val viewModel by activityViewModels<MedicineViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val notificationsPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }
    private val dayAbbreviationMap = mapOf(
        "Mon" to Calendar.MONDAY,
        "Tue" to Calendar.TUESDAY,
        "Wed" to Calendar.WEDNESDAY,
        "Thu" to Calendar.THURSDAY,
        "Fri" to Calendar.FRIDAY,
        "Sat" to Calendar.SATURDAY,
        "Sun" to Calendar.SUNDAY
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    fun initViews() {
        launchPermission()
        sharedViewModel.isFromReminderRV =false
        val mAdapter = MedicineItemRvAdapter(requireContext())
        binding.apply {
            add.setOnClickListener {
                safeNavigate(
                    R.id.action_medicineReminderFragment_to_addMedicineReminderFragment,
                    R.id.medicineReminderFragment
                )
            }
            viewModel.allReminderLiveData.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.placeholder.visible()
                    binding.pointerArrow.visible()
                    mList.clear()
                    mAdapter.updateList(mList)
                } else {
                    binding.placeholder.gone()
                    binding.pointerArrow.gone()
                    mList.clear()
                    mList.addAll(it)
                    //mAdapter.updateList(mList)
                    reminderSorterForFirst(mAdapter)
                }
            }
            reminderSorterForFirst(mAdapter)
            calendar.setOnDateSelectListener { dateModel ->
                lifecycleScope.launch {
                    if (mList.isEmpty()) {
                        delay(1000)
                        reminderSorter(dateModel, mAdapter)
                    } else {
                        delay(150)
                        reminderSorter(dateModel, mAdapter)
                    }
                }
            }
        }

        binding.reminderRv.apply {
            adapter = mAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        mAdapter.onClick = {
            sharedViewModel.isFromReminderRV = true
            sharedViewModel.selectedReminder = it
            safeNavigate(
                R.id.action_medicineReminderFragment_to_addMedicineReminderFragment,
                R.id.medicineReminderFragment
            )
        }

        binding.backBtn.setOnClickListener {
            popBackStack()
        }
    }

    private fun launchPermission() {
        notificationsPermissionLauncher.launch(getPostNotificationsPermission())
    }

    private fun getPostNotificationsPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            "dummy_permission"
        }
    }

    private fun reminderSorter(dateModel: DateModel, mAdapter: MedicineItemRvAdapter) {
        mSortedList.clear()
        try {
            mList.forEach { reminder ->
                if (reminder.days.contains(dayAbbreviationMap[dateModel.dayOfWeek])) {
                    mSortedList.add(reminder)
                }
            }
            if(mSortedList.isEmpty()){
                binding.placeholder.visible()
            } else {
                binding.placeholder.gone()
            }
            mAdapter.updateList(mSortedList)
        } catch (e: Exception) {
            e.localizedMessage
        }
    }

    private fun reminderSorterForFirst(mAdapter: MedicineItemRvAdapter) {
        mSortedList.clear()
        try {
            lifecycleScope.launch {
                mList.forEach { reminder ->
                    if (reminder.days.contains(Calendar.DAY_OF_WEEK)) {
                        mSortedList.add(reminder)
                    }
                }
                if(mSortedList.isEmpty()){
                    binding.placeholder.visible()
                }else{
                    binding.placeholder.gone()
                }
                mAdapter.updateList(mSortedList)
            }
        } catch (e: Exception) {
            e.localizedMessage
        }
    }
}

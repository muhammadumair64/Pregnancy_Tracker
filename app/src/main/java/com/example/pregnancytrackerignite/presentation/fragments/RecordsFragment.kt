package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.databinding.FragmentRecordsBinding
import com.example.pregnancytrackerignite.presentation.adapters.KickRecordRvAdapter
import com.example.pregnancytrackerignite.presentation.adapters.ReportsRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.KickNavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.KickRecordViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.visible

class RecordsFragment : Fragment() {

val binding by lazy {
    FragmentRecordsBinding.inflate(layoutInflater)
}
    val viewModel : SharedViewModel by activityViewModels()
    private val kickViewModel : KickRecordViewModel by activityViewModels()
    private var mList = ArrayList<KickDataClass>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        performNavigation()
        return binding.root
    }

    private fun performNavigation() {
        viewModel.kickCounterNavSelected = {
            when(it) {
                KickNavEvents.NavigateToCounter -> {safeNavigate(R.id.action_recordsFragment_to_counterFragment,R.id.recordsFragment)}
                KickNavEvents.NavigateToRecords -> {}
            }
        }
    }

    fun initViews(){
        val mAdapter = KickRecordRvAdapter(requireContext())
        binding.recordsRV.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        kickViewModel.allReportLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.placeholder.visible()
            } else {
                binding.placeholder.gone()
                mList.clear()
                mList.addAll(it)
                mAdapter.updateList(mList)
            }
        }
    }
}
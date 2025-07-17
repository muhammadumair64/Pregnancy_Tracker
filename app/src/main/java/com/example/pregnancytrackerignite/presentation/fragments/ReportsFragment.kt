package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentReportsBinding
import com.example.pregnancytrackerignite.presentation.adapters.BpHistoryRvAdapter
import com.example.pregnancytrackerignite.presentation.adapters.ReportsRvAdapter
import com.example.pregnancytrackerignite.presentation.fragments.bottomSheet.ReportBottomSheetFragment
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.ReportViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.changeStatusBarColor
import com.iobits.videocompressor.utils.disableMultipleClicking
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.visible
import java.io.File


class ReportsFragment : Fragment() {

    val binding by lazy {
        FragmentReportsBinding.inflate(layoutInflater)
    }
    private var mList = ArrayList<ReportDataClass>()
    private val viewModelReport by activityViewModels<ReportViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        changeStatusBarColor(requireActivity(), R.color.white)
        return binding.root
    }

    fun initViews() {
        val mAdapter = ReportsRvAdapter(requireContext())
        binding.reportRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        mAdapter.onClick = { report ->
            sharedViewModel.selectedFile = report
            safeNavigate(R.id.action_reportsFragment_to_reportDetailsFragment, R.id.reportsFragment)
        }
        mAdapter.onClickMenu = { report, view ->
            openPopupMenu(view, report)
        }
        binding.backBtn.setOnClickListener {
            popBackStack()
        }
        binding.apply {
            addReport.setOnClickListener {
                disableMultipleClicking(it)
                openReportBottomSheet(null)
            }
        }

        viewModelReport.allReportLiveData.observe(viewLifecycleOwner) {
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
                mAdapter.updateList(mList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        changeStatusBarColor(requireActivity(), R.color.status_bar_color)
    }

    private fun openReportBottomSheet(reportDataInstance: ReportDataClass?) {
        val bottomSheetFragment = ReportBottomSheetFragment()
        if(reportDataInstance!= null){
            bottomSheetFragment.setData(reportDataInstance)
        }
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

        bottomSheetFragment.onSave = { report ->
            viewModelReport.addReportModel(report)
        }
    }

    private fun openPopupMenu(it: View, report: ReportDataClass) {
        val popupMenu = PopupMenuCustomLayout(
            requireContext(), R.layout.custome_menu
        ) { item ->
            when(item) {
                R.id.view -> {
                    sharedViewModel.selectedFile = report
                    safeNavigate(
                        R.id.action_reportsFragment_to_reportDetailsFragment,
                        R.id.reportsFragment
                    )
                }

                R.id.edit -> {
                    openReportBottomSheet(report)
                }

                R.id.share -> {
                    // share pdf intent
                    saveAndSharePdf(report.filePath)
                }

                R.id.delete -> {
                    viewModelReport.deleteReportModel(report)
                }
            }
        }
        popupMenu.showSettingDialog(it)
    }

    private fun saveAndSharePdf(filePath: String) {
        try {
            val cacheDir = requireContext().cacheDir
            val fileName = filePath.substringAfterLast("/")
            val destinationFile = File(cacheDir, fileName)

            if (!destinationFile.exists()) {
                Log.d("FileDebug", "File not found in cache. Creating file...")

                destinationFile.outputStream().use { output ->
                    val sampleContent = "This is a test PDF content."
                    output.write(sampleContent.toByteArray())
                }

                Log.d("FileDebug", "File saved at: ${destinationFile.absolutePath}")
            } else {
                Log.d("FileDebug", "File already exists at: ${destinationFile.absolutePath}")
            }

            // Share the file using FileProvider
            val fileUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                destinationFile
            )
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share PDF using"))

        } catch (e: IllegalArgumentException) {
            Log.e("FileDebug", "FileProvider error: ${e.message}")
            showToast("Unable to share file.")
        } catch (e: Exception) {
            Log.e("FileDebug", "Unexpected error: ${e.message}")
            showToast("Unable to share file.")
        }
    }
}

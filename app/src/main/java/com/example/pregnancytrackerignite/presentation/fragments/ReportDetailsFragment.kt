package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.utils.FileHelper
import com.example.pregnancytrackerignite.data.pdfviewer.PdfViewer
import com.example.pregnancytrackerignite.data.pdfviewer.interfaces.OnErrorListener
import com.example.pregnancytrackerignite.data.pdfviewer.interfaces.OnPageChangedListener
import com.example.pregnancytrackerignite.databinding.FragmentReportDetailsBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.changeStatusBarColor
import com.iobits.videocompressor.utils.popBackStack
import kotlinx.coroutines.Dispatchers
import java.lang.Exception


class ReportDetailsFragment : Fragment() , OnPageChangedListener, OnErrorListener {

    val binding by lazy {
        FragmentReportDetailsBinding.inflate(layoutInflater)
    }
    val TAG = "ReportDetailsFragment"
    private val sharedViewModel : SharedViewModel by activityViewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBarColor(requireActivity(), R.color.white)
        initViews()
        return binding.root
    }
    fun initViews() {
        binding.pdfView.removeAllViews()
        if(sharedViewModel.selectedFile?.filePath?.isNotEmpty() == true && sharedViewModel.selectedFile != null){
            binding.apply {
                timeSlot.text = sharedViewModel.selectedFile?.date
                reportTitle.text = sharedViewModel.selectedFile?.title
                notes.text = sharedViewModel.selectedFile?.notes
            }

            PdfViewer.Builder(binding.pdfView, lifecycleScope)
                .setMaxZoom(3f)
                .setZoomEnabled(true)
                .quality(PdfPageQuality.QUALITY_1080)
                .setOnErrorListener(this)
                .setOnPageChangedListener(this)
                .setRenderDispatcher(Dispatchers.Default)
                .build()
                .load(FileHelper.getUriFromPath(sharedViewModel.selectedFile?.filePath))
        }
        binding.backBtn.setOnClickListener {
            popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        changeStatusBarColor(requireActivity(), R.color.status_bar_color)
    }
    //------------------------------------ Pdf Viewer Listener -----------------------------------//
    override fun onPageChanged(page: Int, total: Int) {}

    override fun onFileLoadError(e: Exception) {Log.d(TAG, "onFileLoadError: ${e.localizedMessage}")}

    override fun onAttachViewError(e: Exception) {Log.d(TAG, "onAttachViewError: ${e.localizedMessage}")}

    override fun onPdfRendererError(e: Exception) {Log.d(TAG, "onPdfRendererError: ${e.localizedMessage}")}
}

package com.example.pregnancytrackerignite.presentation.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.data.localDb.ScanDao
import com.example.pregnancytrackerignite.data.models.ScanDataClass
import com.example.pregnancytrackerignite.data.utils.CustomSnapHelper
import com.example.pregnancytrackerignite.data.utils.FileHelper
import com.example.pregnancytrackerignite.data.utils.ScansHelper
import com.example.pregnancytrackerignite.databinding.FragmentScansBinding
import com.example.pregnancytrackerignite.presentation.adapters.WeekRvAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.ScanViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.renderPdfPageToImageView
import com.iobits.videocompressor.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ScansFragment : Fragment() {
    val binding by lazy {
        FragmentScansBinding.inflate(layoutInflater)
    }
    var week = 0
    var currentPosition = 0
    private var mList: ArrayList<String> = ArrayList<String>()
    private var mScans: ArrayList<ScanDataClass> = ArrayList<ScanDataClass>()

    private val viewModelForData: SharedViewModel by activityViewModels()
    private val scanViewModel: ScanViewModel by activityViewModels()

    var uriRealPath = ""

    var TAG = "SCANS_FRAGMENT"

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val mResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                mResult?.pdf?.let { pdf ->
                    val pdfUri = pdf.uri
                    uriRealPath = FileHelper.getRealPathFromURI(requireContext(), pdfUri)
                    if (uriRealPath != "") {
                        saveScan(uriRealPath)
                        //renderPdfPageToImageView(File(uriRealPath), 0, binding.imagePreview)
                    }
                } ?: run {
                    showToast("Cancelled")
                }
            }
        }

    private fun saveScan(uriRealPath: String?) {
        scanViewModel.addScanModel(ScanDataClass(0, uriRealPath.toString(), currentPosition))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getScans()
        initViews()
        return binding.root
    }

    fun initViews() {
        lifecycleScope.launch {
            ScansHelper.addScansInList(requireContext())
        }

        lifecycleScope.launch {
            try {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModelForData.combinedData.collect { combinedData ->
                        week = combinedData?.data?.gestationalAge?.weeks ?: 0
                        binding.apply {
                            weekRv.apply {
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )

                                val adapter = WeekRvAdapter(requireContext(), mList, week - 3)
                                this.adapter = adapter

                                // Attach SnapHelper for center alignment
                                val snapHelper = CustomSnapHelper()
                                if (this.onFlingListener == null) { // Check if SnapHelper is already attached
                                    snapHelper.attachToRecyclerView(this)
                                }

                                // Smooth scroll to initial position
                                delay(500)
                                smoothScrollToPosition(week - 2)

                                // Add listener to detect the center item
                                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(
                                        recyclerView: RecyclerView,
                                        newState: Int
                                    ) {
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            val centerView =
                                                snapHelper.findSnapView(layoutManager) ?: return
                                            val position =
                                                layoutManager?.getPosition(centerView) ?: return

                                            currentPosition = position + 3
                                            // Update the selected item
                                            adapter.updateSelectedPosition(position)

                                            binding.image1.setImageResource(ScansHelper.scanList[position + 2])
                                            setScanImage()
                                            // Scroll to ensure the first or last item can center properly
                                            if (position == 0 || position == adapter.itemCount - 1) {
                                                smoothScrollToPosition(position)
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    }
                }
            }catch (e:Exception){
                e.localizedMessage
            }
        }

        binding.backBtn.setOnClickListener {
            popBackStack()
        }

        binding.pickImage.setOnClickListener {
            startScan()
        }
        mList.apply {
            for (i in 3..40) {
                when (i) {
                    3 -> add("${i}")
                    else -> add("${i}")
                }
            }
        }
    }

    private fun startScan() {
        val options = GmsDocumentScannerOptions.Builder().setGalleryImportAllowed(true)
            .setResultFormats(RESULT_FORMAT_PDF).setScannerMode(SCANNER_MODE_FULL).build()
        val scanner = GmsDocumentScanning.getClient(options)

        scanner.getStartScanIntent(requireActivity()).addOnSuccessListener { intentSender ->
            scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
        }.addOnFailureListener {
            showToast("Failed to start scanning process...")
        }
    }

    private fun getScans() {
        scanViewModel.allScanLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mScans.addAll(it)
                it.forEach { scan ->
                    if (scan.week == currentPosition) {
                        renderPdfPageToImageView(File(scan.filePath), 0, binding.imagePreview)
                    }
                }
            }
        }
    }

    fun setScanImage() {
        for (scan in mScans){
            Log.d(TAG, "setScanImage: My scan ${scan.week}")
            if (scan.week == currentPosition) {
                Log.d(TAG, "setScanImage: true ${scan.week}")
                renderPdfPageToImageView(File(scan.filePath), 0, binding.imagePreview)
                break
            }
            else{
                binding.imagePreview.gone()
            }
        }
    }

}

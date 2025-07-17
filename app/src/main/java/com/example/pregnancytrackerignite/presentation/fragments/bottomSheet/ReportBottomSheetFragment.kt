package com.example.pregnancytrackerignite.presentation.fragments.bottomSheet

import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.example.pregnancytrackerignite.data.models.PregnancyPeriod
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.utils.FileHelper

import com.example.pregnancytrackerignite.databinding.ItemReportBottomSheetBinding
import com.example.pregnancytrackerignite.presentation.viewModel.BloodPressureViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.iobits.rubik_cube_solver.data.utils.isDateWithin9Months
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.videocompressor.utils.renderPdfPageToImageView
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class ReportBottomSheetFragment : BottomSheetDialogFragment() {
    val binding by lazy {
        ItemReportBottomSheetBinding.inflate(layoutInflater)
    }
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var uriRealPath = ""
    private var week = 0
    private val _tempListImages = MutableStateFlow<List<String>>(emptyList())
    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val mResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                mResult?.pdf?.let { pdf ->
                    val pdfUri = pdf.uri
                    uriRealPath = FileHelper.getRealPathFromURI(requireContext(), pdfUri)
                    if (uriRealPath != "") {
                        renderPdfPageToImageView(File(uriRealPath), 0, binding.pdfPreview)
                    }
                } ?: run {
                    showToast("Cancelled")
                }
            }
        }
    var onSave: ((ReportDataClass) -> Unit)? = null

    private var reportData: ReportDataClass? = null

    fun setData(data: ReportDataClass) {
        reportData = data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initListeners()
        reportData?.let {
            binding.nameEt.setText(it.title)
            binding.dateText.text = it.date
            binding.notesEt.setText(it.notes)
            uriRealPath = it.filePath
            renderPdfPageToImageView(File(uriRealPath), 0, binding.pdfPreview)
        }
        return binding.root
    }

    private fun initListeners() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.combinedData.collect { combinedData ->
                    week = combinedData?.data?.gestationalAge?.weeks ?: 0
                }
            }
        }

        binding.apply {
            binding.nameEt.filters = arrayOf(InputFilter.LengthFilter(30))
            binding.notesEt.filters = arrayOf(InputFilter.LengthFilter(250))

            dateBg.setOnClickListener {
                selectDate()
            }
            pickImage.setOnClickListener {
                startScan()
            }
            saveBtn.setOnClickListener {
                if (nameEt.text.toString().isNotEmpty() && dateText.text.toString()
                        .isNotEmpty() && uriRealPath.isNotEmpty()
                ) {
                    val weekString = when (week) {
                        0 -> {
                            "1st Week"
                        }

                        1 -> {
                            "1st Week"
                        }

                        2 -> {
                            "2nd Week"
                        }

                        3 -> {
                            "3rd Week"
                        }

                        else -> {
                            "${week}th Week"
                        }
                    }
                    if (reportData != null) {
                        onSave?.invoke(
                            ReportDataClass(
                                reportData?.id ?: 0,
                                uriRealPath,
                                nameEt.text.toString(),
                                dateText.text.toString(),
                                notesEt.text.toString(),
                                reportData?.week ?:  weekString
                            )
                        )

                    } else {
                        onSave?.invoke(
                            ReportDataClass(
                                0,
                                uriRealPath,
                                nameEt.text.toString(),
                                dateText.text.toString(),
                                notesEt.text.toString(),
                                weekString
                            )
                        )

                    }
                    dismiss()
                } else {
                    showToast("Please Enter data Carefully")
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

    private fun selectDate() {
        val today = CivilCalendar()
        val datePicker = PrimeDatePicker.dialogWith(today).pickSingleDay { day ->
            logd(day.timeInMillis.toString())
            logd(Date(day.timeInMillis).toString())
            if (!day.timeInMillis.isDateWithin9Months()) {
                showToast("Date can't be more than 9 months")
            } else {
                binding.dateText.text = day.longDateString
            }
        }.initiallyPickedSingleDay(today).maxPossibleDate(today).build()
        activity?.supportFragmentManager?.let { fragmentManager ->
            datePicker.show(
                fragmentManager, "Date_Picker_CC"
            )
        }
    }


}

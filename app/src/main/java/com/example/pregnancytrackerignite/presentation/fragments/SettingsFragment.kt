package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentSettingsBinding
import com.example.pregnancytrackerignite.di.myApplication.MyApplication.Companion.isOutForRating
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.rubik_cube_solver.data.utils.toFormattedDateString
import com.iobits.videocompressor.utils.disableMultipleClicking
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showEmailChooser
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private val binding by lazy {
        FragmentSettingsBinding.inflate(layoutInflater)
    }
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initSwitch()
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.apply {
            dateLay.setOnClickListener {
                disableMultipleClicking(it)
                viewModel.isFromSettings = true
                safeNavigate(R.id.action_settingsFragment_to_dueDateFragment, R.id.settingsFragment)
            }
            privacy.setOnClickListener {
                disableMultipleClicking(it)
                showPrivacyPolicy(requireContext())
            }
            share.setOnClickListener {
                disableMultipleClicking(it)
                share()
            }
            backBtn.setOnClickListener {
                disableMultipleClicking(it)
                popBackStack()
            }
            rateus.setOnClickListener {
                disableMultipleClicking(it)
                isOutForRating = true
                val url =
                    "https://play.google.com/store/apps/details?id=com.babytracker.periodtracker.babygenderpredictor.pregnancyapp"
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(url))
                startActivity(i)
            }

            report.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(R.id.action_settingsFragment_to_reportsFragment, R.id.settingsFragment)
            }
            bp.setOnClickListener {
                safeNavigate(
                    R.id.action_settingsFragment_to_bloodPressureHistoryFragment,
                    R.id.settingsFragment
                )
            }
            kick.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_kickCounterFragment,
                    R.id.settingsFragment
                )
            }
            babySize.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_babySizeFragment,
                    R.id.settingsFragment
                )
            }
            reminder.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_medicineReminderFragment,
                    R.id.settingsFragment
                )
            }
            macthScan.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_scansFragment,
                    R.id.settingsFragment
                )
            }
            breathingExercise.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_lungsExerciseFragment,
                    R.id.settingsFragment
                )
            }
            babyNames.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_babyNameFragment,
                    R.id.settingsFragment
                )
            }
            weightTracker.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_weightTrackerFragment,
                    R.id.settingsFragment
                )
            }
            ovuationTracker.setOnClickListener {
                disableMultipleClicking(it)
                safeNavigate(
                    R.id.action_settingsFragment_to_ovulationFragment,
                    R.id.settingsFragment
                )
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.combinedData.collect { combinedData ->
                    binding.dateText.text =
                        combinedData?.data?.estimatedDate?.toFormattedDateString()
                }
            }
        }
    }

    private fun initSwitch() {
        binding.notificationSwitch.trackDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.track)
        binding.notificationSwitch.thumbDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.thumb)
    }


    private fun share() {
        isOutForRating = true
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.setType("text/plain")
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.hey_unleash_the_ultimate_app) +
                    "https://play.google.com/store/apps/details?id=com.babytracker.periodtracker.babygenderpredictor.pregnancyapp"
        )
        val shareIntent = Intent.createChooser(sendIntent, "Shachare via")
        startActivity(shareIntent)
    }

    private fun customerSupport() {
        val supportEmail = "arbax8031@gmail.com" // Replace with your support email address
        val subject = "Support"
        val feedback = getString(R.string.app_name)
        requireContext().showEmailChooser(supportEmail, subject, feedback)
    }

    private fun showPrivacyPolicy(context: Context) {
        if (URLUtil.isValidUrl("https://igniteapps.blogspot.com/2024/10/privacy-policy-for-pregnancy-tracker-app.html")) {
            val i = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://igniteapps.blogspot.com/2024/10/privacy-policy-for-pregnancy-tracker-app.html")
            )
            context.startActivity(i)
        }
    }
}
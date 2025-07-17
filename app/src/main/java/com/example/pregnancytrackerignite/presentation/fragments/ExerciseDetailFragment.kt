package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.datasource.AssetDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentExerciseDetailsBinding
import com.example.pregnancytrackerignite.presentation.viewModel.ExerciseEvents
import com.example.pregnancytrackerignite.presentation.viewModel.ExerciseViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.invisible
import com.iobits.videocompressor.utils.showMoreDialog
import com.iobits.videocompressor.utils.visible
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ExerciseDetailFragment : Fragment() {
    private val binding by lazy {
        FragmentExerciseDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var countDownTimerWithPause: SonicCountDownTimer
    private val viewModel: ExerciseViewModel by viewModels()
    var player : ExoPlayer? = null
    private val viewModelForData: SharedViewModel by activityViewModels()
    private var timeSec: Int = 300

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        Glide.with(requireContext()).load(viewModelForData.selectedExerciseItem?.icon)
//            .into(binding.exerciseImage)
        initViews()
        initTimers()
        initCountdownTimer()
        return binding.root
    }

    private fun initTimers() {
        binding.apply {
            // Set click listeners for all cards
            cardOneMinute.setOnClickListener {
                updateTimerAndUI(1 * 60, cardOneMinute, textOneMinute)
                updateSelectedTime(1 * 60)
            }
            cardFiveMinute.setOnClickListener {
                updateTimerAndUI(5 * 60, cardFiveMinute, textFiveMinute)
                updateSelectedTime(5 * 60)
            }
            cardTenMinute.setOnClickListener {
                updateTimerAndUI(10 * 60, cardTenMinute, textTenMinute)
                updateSelectedTime(10 * 60)
            }
            cardFifteenMinute.setOnClickListener {
                updateTimerAndUI(15 * 60, cardFifteenMinute, textFifteenMinute)
                updateSelectedTime(15 * 60)
            }
            cardTewntyMinute.setOnClickListener {
                updateTimerAndUI(20 * 60, cardTewntyMinute, textTwentyMinute)
                updateSelectedTime(20 * 60)
            }
        }
    }

    private fun updateSelectedTime(selectedTimeInSeconds: Int) {
        timeSec = selectedTimeInSeconds

        val minutes = timeSec / 60
        val seconds = timeSec % 60
        binding.tvTimerCountFinished.text = "${minutes}m ${seconds}s"
    }

    private fun updateTimerAndUI(newTimeSec: Int, selectedCard: CardView, selectedText: TextView) {
        timeSec = newTimeSec
        // Reset all card colors and text colors
        binding.apply {
            listOf(cardOneMinute, cardFiveMinute, cardTenMinute, cardFifteenMinute, cardTewntyMinute).forEach {
                it.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
            listOf(textOneMinute, textFiveMinute, textTenMinute, textFifteenMinute, textTwentyMinute).forEach {
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }

            // Highlight the selected card and text
            selectedCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            selectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        // Reset the timer and progress
        resetTimerAndProgress()
    }

    override fun onPause() {
        super.onPause()
        try {
            player?.pause()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            countDownTimerWithPause.cancelCountDownTimer()
            player?.pause()
            player?.release()
        } catch(e:Exception){
            e.printStackTrace()
        }
    }

    private fun initViews() {
        binding.apply {
            tvTimerCountFinished.text = "5m 0s"

            val description = viewModelForData.selectedExerciseItem?.description ?: ""
            val readMoreText = " Read more"
            createSpannableWithReadMore(description, readMoreText, instructionText)

            headers.apply {
                title.text = buildString {
                    append(viewModelForData.selectedExerciseItem?.name ?: "Exercise")
                }
                backBtn.setOnClickListener {
                    findNavController().navigateUp()
                }
            }
            try {
                initializePlayer(viewModelForData.selectedExerciseItem!!.videoName)
            }catch (e:Exception){
                e.printStackTrace()
            }

            btnPlaypauseTimer.setOnClickListener {
                linearLayout2.gone()
                if (countDownTimerWithPause.isTimerRunning()) {
                    if (countDownTimerWithPause.isTimerPaused()) {
                        // Resume the timer
                        viewModel.startTimer()
                        player?.play()
                        countDownTimerWithPause.resumeCountDownTimer()
                        btnPlaypauseTimer.setImageResource(R.drawable.ic_baseline_pause_24)
                    } else {
                        // Pause the timer
                        countDownTimerWithPause.pauseCountDownTimer()
                        btnPlaypauseTimer.setImageResource(R.drawable.ic_baseline_play_24)
                        player?.pause()
                    }
                } else {
                    // Start the timer only if it's not already running
                    viewModel.startTimer()
                    player?.play()
                    countDownTimerWithPause.startCountDownTimer()
                    btnPlaypauseTimer.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            binding.btnReset.setOnClickListener {
                linearLayout2.visible()
                viewModel.resetTimer()
                player?.apply {
                    seekTo(0)
                    pause()
                }
                countDownTimerWithPause.stopCountDownTimer()

                // Reset progress bar
                binding.progressIndicator.progress = 0

                // Set tvTimerCountFinished to the selected time
                val minutes = timeSec / 60
                val seconds = timeSec % 60
                binding.tvTimerCountFinished.text = "${minutes}m ${seconds}s"
            }

            binding.btnStop
                .setOnClickListener {
                linearLayout2.visible()
                viewModel.resetTimer()
                player?.apply {
                    seekTo(0)
                    pause()
                }
                countDownTimerWithPause.stopCountDownTimer()

                // Reset progress bar
                binding.progressIndicator.progress = 0

                // Set tvTimerCountFinished to the selected time
                val minutes = timeSec / 60
                val seconds = timeSec % 60
                binding.tvTimerCountFinished.text = "${minutes}m ${seconds}s"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCountdownTimer() {
        binding.progressIndicator.max = timeSec

        countDownTimerWithPause = object : SonicCountDownTimer((timeSec * 1000).toLong(), 1000) {

            override fun onTimerTick(timeRemaining: Long) {
                // Calculate the remaining minutes and seconds
                val remainingSeconds = (timeRemaining / 1000).toInt()
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60

                // Format the time as "Xm Ys"
                val formattedTime = "${minutes}m ${seconds}s"

                // Update the UI
                binding.progressIndicator.progress = timeSec - remainingSeconds // Update progress based on time passed
                binding.tvTimerCountFinished.text = formattedTime
            }

            override fun onTimerFinish() {
                // Reset play/pause button to "play" state
                binding.btnPlaypauseTimer.setImageResource(R.drawable.ic_baseline_play_24)
            }
        }
    }

    private fun resetTimerAndProgress() {
        countDownTimerWithPause.stopCountDownTimer() // Stop any running timer
        binding.progressIndicator.max = timeSec
        binding.progressIndicator.progress = 0
        binding.tvTimerCountFinished.text = "0m 0s"
        initCountdownTimer() // Reinitialize the countdown timer with the new time
    }

    private fun getAssetUri(context: Context, assetFileName: String): Uri? {
        val file = File(context.filesDir, assetFileName)
        if (!file.exists()) {
            try {
                val inputStream: InputStream = context.assets.open(assetFileName)
                val outputStream: OutputStream = context.openFileOutput(assetFileName, Context.MODE_PRIVATE)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }
        return Uri.fromFile(file)
    }

    private fun initializePlayer(fileName:Int) {
        lifecycleScope.launch {
            binding.apply {
                // Initialize the ExoPlayer instance
                player = ExoPlayer.Builder(requireContext()).build()
                playerView.player = player
                // Get the URI of the video from the raw folder
                val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${fileName}")
                // Create a MediaItem for the video
                val mediaItem = MediaItem.fromUri(videoUri)

                // Add the MediaItem to the player
                player?.setMediaItem(mediaItem)

                // Enable looping
                player?.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                player?.prepare() }
        }
    }

    private fun createSpannableWithReadMore(
        description: String,
        readMoreText: String,
        instructionText: TextView
    ) {
        val words = description.split(" ")
        var truncatedDescription = words.take(20).joinToString(" ")
        if (words.size > 25) {
            truncatedDescription += "... "
            val spannableString = SpannableStringBuilder(truncatedDescription + readMoreText)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    instructionText.text = description
                    instructionText.movementMethod = null
                    instructionText.highlightColor = Color.TRANSPARENT
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ds.linkColor // Keep the text color same as link color (optional)
                    ds.isUnderlineText = true // Enable underline if needed
                    ds.bgColor = Color.TRANSPARENT // Set background color to transparent to avoid highlight
                }
            }

            spannableString.setSpan(
                clickableSpan,
                spannableString.length - readMoreText.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Disable the TextView's highlight color when the span is clicked
            instructionText.highlightColor = Color.TRANSPARENT
            instructionText.movementMethod = LinkMovementMethod.getInstance()
            instructionText.text = spannableString
        } else {
            instructionText.text = description // If less than 25 words, just show the full description
        }
    }
}

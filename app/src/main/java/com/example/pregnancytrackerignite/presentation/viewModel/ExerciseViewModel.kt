package com.example.pregnancytrackerignite.presentation.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState = _uiState.asStateFlow()

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = TIMER_DURATION
    private var isPaused = false

    companion object {
        const val TIMER_DURATION = 20 * 60 * 1000L
    }

    fun onEvent(event: ExerciseEvents) {
        when (event) {
            ExerciseEvents.OnStartPauseResumeButton -> {
                when {
                    !_uiState.value.isTimerStarted -> startTimer()
                    _uiState.value.isTimerPaused -> resumeTimer()
                    else -> pauseTimer()
                }
            }

            ExerciseEvents.ResetTimer -> {
                resetTimer()
            }
        }
    }

 fun startTimer() {
        isPaused = false
        timer = object : CountDownTimer(timeLeftInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60

                _uiState.update {
                    it.copy(
                        progressValue = ((TIMER_DURATION - millisUntilFinished) / 1000).toInt(),
                        isTimerStarted = true,
                        isTimerPaused = false,
                        timeLeft = String.format(buildString {
                            append("%02d:%02d")
                        }, minutes, seconds),
                        buttonText = "Pause"
                    )
                }
            }

            override fun onFinish() {
                _uiState.update {
                    it.copy(
                        isTimerStarted = false, progressValue = 100, // Timer finished
                        timeLeft = "00:00", buttonText = "Start"
                    )
                }
            }
        }.start()
    }

    fun pauseTimer() {
        isPaused = true
        timer?.cancel()
        _uiState.update {
            it.copy(
                isTimerPaused = true, buttonText = "Resume"
            )
        }
    }

    private fun resumeTimer() {
        if (isPaused) {
            startTimer() // Start timer with remaining time
            _uiState.update {
                it.copy(
                    buttonText = "Pause"
                )
            }
        }
    }

   fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = TIMER_DURATION
        isPaused = false
        _uiState.update {
            it.copy(
                progressValue = 0,
                isTimerStarted = false,
                isTimerPaused = false,
                timeLeft = "20:00",
                buttonText = "Start"
            )
        }
    }
}

data class ExerciseUiState(
    val isTimerStarted: Boolean = false,
    val isTimerPaused: Boolean = false,
    val progressValue: Int = 0,
    val timeLeft: String = "20:00",
    val buttonText: String = "Start"
)

sealed interface ExerciseEvents {
    data object OnStartPauseResumeButton : ExerciseEvents
    data object ResetTimer : ExerciseEvents
}
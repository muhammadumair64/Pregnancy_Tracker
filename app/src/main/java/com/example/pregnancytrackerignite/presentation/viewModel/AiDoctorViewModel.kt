package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.iobits.rubik_cube_solver.data.utils.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AiDoctorViewModel @Inject constructor() : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private var responseJob: Job? = null
    var prompt = ""

    fun onEvent(event: ChatEvents) {
        when (event) {
            is ChatEvents.OnMessageSend -> {
                if (_chatState.value.isLoading) {
                    _chatState.update {
                        it.copy(errorMessage = "Please wait for the current response to complete.")
                    }
                    return
                }
                _chatState.update {
                    it.copy(messages = it.messages + event.message, isLoading = true, errorMessage = null)
                }
                startResponseTask(event.message.message)
            }

            ChatEvents.CancelResponse -> {
                cancelResponseTask()
            }
        }
    }

    private fun startResponseTask(prompt: String) {
        val prefix = "Answer the following question with medical knowledge and pregnancy-related advice if applicable also if question is not medical related then ignore pregnancy related info also dont tell me that im not a doctor so i have no knowledge i know that u are not im just asking if u have some info over internet or so then provide Your response should not longer then 12 lines add some bullets if possible, ans most revelant info and at last ask question from user relevant to response in bold:"

        responseJob = viewModelScope.launch {
            try {
                val response = getResponse(prefix+prompt)
                _chatState.update {
                    it.copy(
                        messages = it.messages + Message(response, MsgType.RECEIVER),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                _chatState.update {
                    it.copy(isLoading = false, errorMessage = "Response canceled.")
                }
            } catch (e: Exception) {
                logd(e.message.toString())
                _chatState.update {
                    it.copy(isLoading = false, errorMessage = "Error generating response.")
                }
            }
        }
    }

    private suspend fun getResponse(prompt: String): String {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyBGpKYyY1zjq3kFKIEVs1bTs7QkLND6Z9s",
        )
        return withContext(Dispatchers.IO) {
            generativeModel.generateContent(prompt).text ?: throw Exception("Error generating response")
        }
    }

    private fun cancelResponseTask() {
        responseJob?.cancel()
        responseJob = null
        _chatState.update {
            it.copy(isLoading = false, errorMessage = "Response generation canceled.")
        }
    }
}

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ChatEvents {
    data class OnMessageSend(val message: Message) : ChatEvents
    data object CancelResponse : ChatEvents
}

data class Message(val message: String, val msgType: MsgType = MsgType.SENDER)

enum class MsgType {
    SENDER, RECEIVER
}
package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.applovin.impl.ui
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentAiDoctorBinding
import com.example.pregnancytrackerignite.presentation.adapters.MessagesAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.AiDoctorViewModel
import com.example.pregnancytrackerignite.presentation.viewModel.ChatEvents
import com.example.pregnancytrackerignite.presentation.viewModel.Message
import com.example.pregnancytrackerignite.presentation.viewModel.MsgType
import com.google.android.material.snackbar.Snackbar
import com.iobits.rubik_cube_solver.data.utils.logd
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.hideKeyboard
import com.iobits.videocompressor.utils.onDone
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.textWatcher
import com.iobits.videocompressor.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiDoctorFragment : Fragment() {
    private val binding by lazy {
        FragmentAiDoctorBinding.inflate(layoutInflater)
    }
    private val viewModel: AiDoctorViewModel by activityViewModels()
    private val adapter by lazy {
        MessagesAdapter(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        handleBackPress {
            safeNavigate(R.id.action_aiDoctorFragment_to_mainFragment,R.id.aiDoctorFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
            delay(1000)
            viewModel.onEvent(
                ChatEvents.OnMessageSend(
                    Message(
                        viewModel.prompt, msgType = MsgType.SENDER
                    )
                )
            )
        }

        binding.apply {
            backBtn.setOnClickListener {
//                findNavController().navigateUp()
                safeNavigate(R.id.action_aiDoctorFragment_to_mainFragment,R.id.aiDoctorFragment)
            }
            editTextMessages.apply {
                onDone {
                    hideKeyboard(view)
                    clearFocus()
                }
                setOnFocusChangeListener { _, hasFocus ->
                    typeMessageCard.strokeColor = if (hasFocus) {
                        ContextCompat.getColor(requireContext(), R.color.theme_text_color)
                    } else {
                        ContextCompat.getColor(requireContext(), android.R.color.transparent)
                    }
                    typeMessageCard.setCardBackgroundColor( if (hasFocus) {
                        binding.apply {
                            view11.gone()
                            view12.gone()
                            imageView23.gone()
                        }
                        ContextCompat.getColorStateList(requireContext(), R.color.extra_light_pink)
                    } else {
                        binding.apply {
                            view11.visible()
                            view12.visible()
                        }
                        ContextCompat.getColorStateList(requireContext(), R.color.field_bg_color)
                    })
                }
                textWatcher { message ->
                    message?.let {
                        sendMessage.apply {
                            alpha = 1f
                            isClickable = true
                        }
                        typeMessageCard.strokeColor =
                            ContextCompat.getColor(requireContext(), R.color.theme_text_color)
                    } ?: run {
                        sendMessage.apply {
                            isClickable = false
                            alpha = 0.5f
                        }
                        typeMessageCard.strokeColor =
                            ContextCompat.getColor(requireContext(), android.R.color.transparent)

                    }
                }
            }
            sendMessage.setOnClickListener {
                if (editTextMessages.text.isEmpty()) {
                    showToast("Message cannot be empty")
                    return@setOnClickListener
                }
                viewModel.onEvent(
                    ChatEvents.OnMessageSend(
                        Message(
                            editTextMessages.text.toString(), msgType = MsgType.SENDER
                        )
                    )
                )
                editTextMessages.apply {
                    text.clear()
                    hideKeyboard(view)
                    clearFocus()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.apply {
                viewModel.chatState.collect { uiState ->
                    try {
                        logd(uiState.toString())
                        uiState.errorMessage?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                        }
                    }catch (e:Exception){
                        e.localizedMessage
                    }
                    stopGenerating.apply {
                        if (uiState.isLoading) visible() else gone()
                        textView18.text = "Processing On Request"
                       lifecycleScope.launch {
                            delay(300)
                            textView18.text = "Generating Answer"
                        }
                        setOnClickListener {
                            viewModel.onEvent(ChatEvents.CancelResponse)
                        }
                    }
                    if (uiState.messages.isEmpty()) {
                        whenNoMessages.visible()
                        messagesRV.gone()
                    } else {
                        messagesRV.adapter = adapter
                        whenNoMessages.gone()
                        messagesRV.visible()
                        adapter.submitMessages(uiState.messages)
                        val layoutManager = messagesRV.layoutManager as? LinearLayoutManager
                        layoutManager?.scrollToPositionWithOffset(
                            uiState.messages.lastIndex,
                            -50
                        )
                    }
                }
            }
        }
    }
}


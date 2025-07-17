package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.databinding.ReceiversMessageLayoutBinding
import com.example.pregnancytrackerignite.databinding.SenderMessageLayoutBinding
import com.example.pregnancytrackerignite.presentation.viewModel.Message
import com.example.pregnancytrackerignite.presentation.viewModel.MsgType

import io.noties.markwon.Markwon

class MessagesAdapter(private val context: android.content.Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()

    // Define view types
    companion object {
        private const val VIEW_TYPE_SENDER = 0
        private const val VIEW_TYPE_RECEIVER = 1
    }

    // Update the list of messages
    fun submitMessages(newMessages: List<Message> ) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].msgType) {
            MsgType.SENDER -> VIEW_TYPE_SENDER
            MsgType.RECEIVER -> VIEW_TYPE_RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> {
                val binding = SenderMessageLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SenderViewHolder(binding)
            }

            VIEW_TYPE_RECEIVER -> {
                val binding = ReceiversMessageLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ReceiverViewHolder(context,binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SenderViewHolder -> holder.bind(message)
            is ReceiverViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class SenderViewHolder(private val binding: SenderMessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.apply {
                senderMsg.text = message.message
            }
        }
    }

    class ReceiverViewHolder(private val context: Context,private val binding: ReceiversMessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.apply {
                receiversMsg.apply {
                    val txt=message.message.trimIndent()
                    val markwon = Markwon.create(context)
                    markwon.setMarkdown(this, txt)
                }
                copyText.setOnClickListener {
                    val clipboard =
                        binding.root.context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText("Copied Text", message.message)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(binding.root.context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

                }
                shareText.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, message.message)
                    }
                    val chooserIntent = Intent.createChooser(shareIntent, "Share Message")
                    context.startActivity(chooserIntent)
                }
            }
        }
    }

}

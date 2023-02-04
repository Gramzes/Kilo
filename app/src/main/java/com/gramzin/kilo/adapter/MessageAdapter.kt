package com.gramzin.kilo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.MessageInItemBinding
import com.gramzin.kilo.databinding.MessageOutItemBinding
import com.gramzin.kilo.model.Message
import java.lang.Exception

class MessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = arrayListOf<Message>()

    private interface Bindable {
        fun bind(message: Message)
    }

    class MessageInHolder(private val binding: MessageInItemBinding) :
        RecyclerView.ViewHolder(binding.root), Bindable {
        override fun bind(message: Message) {
            binding.messageText.text = message.text.toString()
            binding.messageTime.text = message.getTimeDate()
            Glide.with(binding.messageImage)
                .load(message.imageURL)
                .into(binding.messageImage)
        }
    }

    class MessageOutHolder(private val binding: MessageOutItemBinding) :
        RecyclerView.ViewHolder(binding.root), Bindable {
        override fun bind(message: Message) {
            binding.messageText.text = message.text.toString()
            binding.messageTime.text = message.getTimeDate()
            Glide.with(binding.messageImage)
                .load(message.imageURL)
                .into(binding.messageImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Message.MESSAGE_IN -> {
                val binding =
                    MessageInItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MessageInHolder(binding)
            }
            Message.MESSAGE_OUT -> {
                val binding = MessageOutItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MessageOutHolder(binding)
            }
            else -> throw Exception("Invalid message type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Bindable).bind(messages[position])
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int) = messages[position].type!!

    fun addMessage(message: Message){
        messages.add(message)
        notifyItemInserted(messages.size-1)
    }
    fun changeData(key: String){
        val i = messages.indexOfFirst { it.messageId == key }
        notifyItemChanged(i)
    }

    fun changeData(message: Message){
        if (message.messageId!=null) {
            val i = messages.indexOfFirst { it.messageId == message.messageId }
            messages[i] = message
            notifyItemChanged(i)
        }
    }
}
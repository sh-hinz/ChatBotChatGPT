package com.example.chatbotchatgpt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotchatgpt.R
import com.example.chatbotchatgpt.data.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ItemViewHolder>() {
    private var dataset = mutableListOf<ChatMessage>()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userMsgTextView: TextView = view.findViewById(R.id.userChatTextView)
        var botMsgTextView: TextView = view.findViewById(R.id.botChatTextView)
        var userTimeTextView: TextView = view.findViewById(R.id.userChatTimeTextView)
        var botTimeTextView: TextView = view.findViewById(R.id.botChatTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val message = dataset[position]

        val sentTime = message.sentTime
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault()) // Uhr ok so?
        val formattedTime = timeFormatter.format(sentTime)

        if(message.sentBy == ChatMessage.SENT_BY_ME) {
            holder.userMsgTextView.visibility = View.VISIBLE
            holder.userTimeTextView.visibility = View.VISIBLE
            holder.botMsgTextView.visibility = View.GONE
            holder.botTimeTextView.visibility = View.GONE
            holder.userMsgTextView.text = message.message
            holder.userTimeTextView.text = buildString {
                append(formattedTime)
                append(" Uhr")
            }
        } else {
            holder.userMsgTextView.visibility = View.GONE
            holder.userTimeTextView.visibility = View.GONE
            holder.botMsgTextView.visibility = View.VISIBLE
            holder.botTimeTextView.visibility = View.VISIBLE
            holder.botMsgTextView.text = message.message
            holder.botTimeTextView.text = buildString {
                append(formattedTime)
                append(" Uhr")
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun loadMessages(messages: MutableList<ChatMessage>) {
        this.dataset = messages
        Log.d("loadMessages", "$dataset")
        notifyItemRangeChanged(0,dataset.size)
    }
}
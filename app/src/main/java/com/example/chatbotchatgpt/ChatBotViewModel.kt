package com.example.chatbotchatgpt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatbotchatgpt.data.Repository
import com.example.chatbotchatgpt.data.model.ChatMessage

class ChatBotViewModel : ViewModel() {
    var repository = Repository()
    private var _messageList = MutableLiveData<MutableList<ChatMessage>>()
    val messageList: LiveData<MutableList<ChatMessage>>
        get() = _messageList

    fun sendMessage(message: ChatMessage) {
        _messageList.value?.add(message)
        _messageList.value = _messageList.value
    }

    init {
        _messageList.value = repository.loadMessages()
    }
}
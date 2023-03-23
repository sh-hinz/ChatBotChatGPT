package com.example.chatbotchatgpt.data.model

import java.util.*

class ChatMessage(
    var message: String,
    var sentTime: Date,
    var sentBy: String
) {

    companion object {
        var SENT_BY_ME = "Sie"
        var SENT_BY_BOT = "Chatbot"
    }
}
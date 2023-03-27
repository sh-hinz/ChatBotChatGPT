package com.example.chatbotchatgpt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotchatgpt.BuildConfig.OPENAI_API_KEY
import com.example.chatbotchatgpt.adapter.ChatAdapter
import com.example.chatbotchatgpt.data.Repository
import com.example.chatbotchatgpt.data.model.ChatMessage
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ChatBotViewModel : ViewModel() {
    var repository = Repository()
    private var _messageList = MutableLiveData<MutableList<ChatMessage>>()
    val messageList: LiveData<MutableList<ChatMessage>>
        get() = _messageList

    fun sendMessage(message: ChatMessage) {
        viewModelScope.launch {
            _messageList.value?.add(message)
        }
    }

    private fun addMessageToList(message: ChatMessage) {
        viewModelScope.launch {
            _messageList.value?.add(message)
            _messageList.value = _messageList.value
        }
    }

    fun sendResponse(response: String?) {
        response?.let { ChatMessage(it, Date(), ChatMessage.SENT_BY_BOT) }?.let { addMessageToList(it) }
    }

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    var client = OkHttpClient()

    fun callAPI(question: String) {
        var jsonBody: JSONObject = JSONObject()
        try {
            jsonBody.put("model","text-davinci-003")
            jsonBody.put("prompt",question)
            jsonBody.put("max_tokens",2048)
            jsonBody.put("temperature",0)
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("callAPI - JSON", "[ERROR] $e")
        }

        var body = jsonBody.toString().toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer $OPENAI_API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                sendResponse("[ERROR] " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.body!!.string())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getString("text")
                        sendResponse(result.trim { it <= ' ' })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    sendResponse("[ERROR] " + response.body.toString())
                }
            }
        })
    }

    init {
        _messageList.value = repository.loadMessages()
    }
}
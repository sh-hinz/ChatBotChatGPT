package com.example.chatbotchatgpt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chatbotchatgpt.ChatBotViewModel
import com.example.chatbotchatgpt.adapter.ChatAdapter
import com.example.chatbotchatgpt.data.model.ChatMessage
import com.example.chatbotchatgpt.databinding.FragmentChatBinding
import java.util.*

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val chatAdapter: ChatAdapter by lazy { ChatAdapter() }
    private val viewModel: ChatBotViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.chatFragmentRecyclerView
        val messageField = binding.chatFragmentTextInputLayout
        val messageFieldEditText = binding.chatFragmentTextInputLayoutEditText
        val sendButton = binding.sendImageButton
        val toolbar = binding.chatMaterialToolbar

        recyclerView.adapter = chatAdapter

        viewModel.messageList.observe(viewLifecycleOwner) { messages ->
            chatAdapter.loadMessages(messages)
        }

        sendButton.setOnClickListener {
            if (!messageFieldEditText.text.isNullOrEmpty()) {
                val message = messageFieldEditText.text.toString()
                sendMessage(message, ChatMessage.SENT_BY_ME)
                //Toast.makeText(requireContext(), messageFieldEditText.text.toString(),Toast.LENGTH_SHORT).show() // lol, causes APK Errors
                viewModel.callAPI(message)
                messageFieldEditText.text?.clear()
            }
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendMessage(message: String, sentBy: String) {
        viewModel.sendMessage(ChatMessage(message, Date(), sentBy))
        chatAdapter.notifyItemInserted(viewModel.messageList.value!!.lastIndex) // Oder DatasetChanged?
        //binding.chatFragmentRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }
}
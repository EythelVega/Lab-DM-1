package com.bav.labdispositivosmovilesbav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.bav.labdispositivosmovilesbav.repository.ChatRepository
import com.bav.labdispositivosmovilesbav.models.ChatUser
import com.bav.labdispositivosmovilesbav.models.Message

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    
    private val _users = MutableStateFlow<List<ChatUser>>(emptyList())
    val users: StateFlow<List<ChatUser>> = _users.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        repository.getAllUsers { usersList ->
            _users.value = usersList
        }
    }

    fun sendMessage(message: String, receiverId: String?) {
        receiverId?.let { id ->
            repository.sendMessage(message, id)
        }
    }

    fun startListeningMessages(receiverId: String?) {
        receiverId?.let { id ->
            repository.getMessages(id) { messagesList ->
                _messages.value = messagesList
            }
        }
    }
} 
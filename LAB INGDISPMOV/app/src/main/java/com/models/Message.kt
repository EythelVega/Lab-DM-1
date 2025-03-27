package com.bav.labdispositivosmovilesbav.models

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
) 
package com.models

data class Notification(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val type: NotificationType = NotificationType.NEW_PRODUCT,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val createdBy: String = "" // UID del administrador que la creó
)

enum class NotificationType {
    NEW_PRODUCT,
    DISCOUNT,
    ARRIVAL,
    EVENT,
    INFO
}

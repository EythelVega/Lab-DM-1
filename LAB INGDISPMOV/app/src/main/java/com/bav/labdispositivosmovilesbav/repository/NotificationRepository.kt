package com.bav.labdispositivosmovilesbav.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.models.Notification
import com.models.NotificationType
import kotlinx.coroutines.tasks.await

class NotificationRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllNotifications(): List<Notification> {
        return try {
            val snapshot = db.collection("notifications")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val createdAt = doc.getTimestamp("createdAt")?.toDate()?.time ?: System.currentTimeMillis()

                Notification(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    content = doc.getString("content") ?: "",
                    type = NotificationType.valueOf(
                        doc.getString("type") ?: NotificationType.INFO.name
                    ),
                    isRead = doc.getBoolean("isRead") ?: false,
                    createdAt = createdAt,
                    createdBy = doc.getString("createdBy") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error obteniendo notificaciones: ${e.message}")
            emptyList()
        }
    }

    suspend fun getUnreadNotifications(): List<Notification> {
        return try {
            val snapshot = db.collection("notifications")
                .whereEqualTo("isRead", false)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val createdAt = doc.getTimestamp("createdAt")?.toDate()?.time ?: System.currentTimeMillis()

                Notification(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    content = doc.getString("content") ?: "",
                    type = NotificationType.valueOf(
                        doc.getString("type") ?: NotificationType.INFO.name
                    ),
                    isRead = doc.getBoolean("isRead") ?: false,
                    createdAt = createdAt,
                    createdBy = doc.getString("createdBy") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error obteniendo notificaciones no leídas: ${e.message}")
            emptyList()
        }
    }

    suspend fun addNotification(notification: Notification): Result<String> {
        return try {
            val notificationMap = hashMapOf<String, Any>(
                "title" to notification.title,
                "content" to notification.content,
                "type" to notification.type.name,
                "isRead" to notification.isRead,
                "createdAt" to Timestamp.now(),
                "createdBy" to notification.createdBy
            )

            val documentRef = db.collection("notifications")
                .add(notificationMap)
                .await()

            Log.d("NotificationRepository", "Notificación añadida con ID: ${documentRef.id}")
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error añadiendo notificación: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun addNotificationSync(notification: Notification): String? {
        return try {
            val notificationMap = hashMapOf<String, Any>(
                "title" to notification.title,
                "content" to notification.content,
                "type" to notification.type.name,
                "isRead" to notification.isRead,
                "createdAt" to Timestamp.now(),
                "createdBy" to notification.createdBy
            )

            val documentRef = db.collection("notifications")
                .add(notificationMap)
                .await()

            Log.d("NotificationRepository", "Notificación añadida con ID: ${documentRef.id}")
            documentRef.id
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error añadiendo notificación: ${e.message}")
            null
        }
    }

    suspend fun markAsRead(notificationId: String): Result<Unit> {
        return try {
            db.collection("notifications")
                .document(notificationId)
                .update("isRead", true)
                .await()

            Log.d("NotificationRepository", "Notificación marcada como leída: $notificationId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error marcando notificación como leída: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return try {
            db.collection("notifications")
                .document(notificationId)
                .delete()
                .await()

            Log.d("NotificationRepository", "Notificación eliminada: $notificationId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error eliminando notificación: ${e.message}")
            Result.failure(e)
        }
    }
}

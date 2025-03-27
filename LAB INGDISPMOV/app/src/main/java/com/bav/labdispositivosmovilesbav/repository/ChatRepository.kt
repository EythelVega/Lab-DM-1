package com.bav.labdispositivosmovilesbav.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.bav.labdispositivosmovilesbav.models.ChatUser
import com.bav.labdispositivosmovilesbav.models.Message

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getAllUsers(onSuccess: (List<ChatUser>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { doc ->
                    if (doc.id != auth.currentUser?.uid) {
                        ChatUser(
                            userId = doc.id,
                            name = doc.getString("name") ?: "",
                            email = doc.getString("email") ?: ""
                        )
                    } else null
                }
                onSuccess(users)
            }
            .addOnFailureListener { e ->
                Log.e("ChatRepository", "Error obteniendo usuarios: ${e.message}")
                onSuccess(emptyList())
            }
    }

    fun sendMessage(message: String, receiverId: String) {
        val currentUser = auth.currentUser ?: return
        
        val newMessage = Message(
            senderId = currentUser.uid,
            senderName = currentUser.displayName ?: "Usuario",
            message = message
        )

        db.collection("chats")
            .document(getChatRoomId(currentUser.uid, receiverId))
            .collection("messages")
            .add(newMessage)
            .addOnFailureListener { e ->
                Log.e("ChatRepository", "Error enviando mensaje: ${e.message}")
            }
    }

    fun getMessages(receiverId: String, onMessagesUpdated: (List<Message>) -> Unit) {
        val currentUser = auth.currentUser ?: return
        
        db.collection("chats")
            .document(getChatRoomId(currentUser.uid, receiverId))
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatRepository", "Error escuchando mensajes: ${error.message}")
                    onMessagesUpdated(emptyList())
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                } ?: emptyList()
                
                onMessagesUpdated(messages)
            }
    }

    private fun getChatRoomId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "${userId1}_${userId2}"
        } else {
            "${userId2}_${userId1}"
        }
    }
} 
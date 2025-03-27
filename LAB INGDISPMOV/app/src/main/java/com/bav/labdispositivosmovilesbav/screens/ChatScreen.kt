package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.viewmodels.ChatViewModel
import com.bav.labdispositivosmovilesbav.models.Message
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    receiverId: String? = null
) {
    var messageText by remember { mutableStateOf("") }
    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(receiverId) {
        viewModel.startListeningMessages(receiverId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Chat") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            }
        )

        // Lista de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                MessageItem(message)
            }
        }

        // Campo de entrada de mensaje
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Mensaje...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText, receiverId)
                        messageText = ""
                    }
                }
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 
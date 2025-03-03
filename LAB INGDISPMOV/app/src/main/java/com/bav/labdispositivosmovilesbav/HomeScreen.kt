package com.bav.labdispositivosmovilesbav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensajes") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("new_chat")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo chat")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ChatList(navController)
        }
    }
}

@Composable
fun ChatList(navController: NavController) {
    val chats = listOf("Juan", "Ana", "Pedro", "María", "Carlos")

    LazyColumn {
        items(chats) { chat ->
            ChatItem(name = chat) {
                navController.navigate("chat/$chat")
            }
        }
    }
}

@Composable
fun ChatItem(name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
    }
}

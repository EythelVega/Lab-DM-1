package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.components.BottomNavigationBar
import com.bav.labdispositivosmovilesbav.repository.NotificationRepository
import com.models.Notification
import com.models.NotificationType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageNotificationsScreen(navController: NavController) {
    val repository = remember { NotificationRepository() }
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        notifications = repository.getAllNotifications()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.clickable { navController.navigateUp() }
                        )
                        Text(
                            "Gestionar Notificaciones",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Crear Notificación", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFF004E89))
                        )
                    )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, "manage_notifications", "Administrador")
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Text(
                        "No hay notificaciones",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F9FA)),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationManagementCard(
                        notification = notification,
                        onDelete = {
                            scope.launch {
                                repository.deleteNotification(notification.id)
                                notifications = repository.getAllNotifications()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddNotificationDialog(
            onDismiss = { showAddDialog = false },
            onSave = { notification ->
                scope.launch {
                    val result = repository.addNotificationSync(notification)
                    if (result != null) {
                        notifications = repository.getAllNotifications()
                        showAddDialog = false
                    }
                }
            }
        )
    }
}

@Composable
fun NotificationManagementCard(
    notification: Notification,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = notification.content,
                    fontSize = 14.sp,
                    color = Color(0xFF718096),
                    maxLines = 2
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNotificationDialog(
    onDismiss: () -> Unit,
    onSave: (Notification) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(NotificationType.NEW_PRODUCT) }
    val auth = FirebaseAuth.getInstance()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Notificación") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector de tipo
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tipo de notificación", fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = type == NotificationType.NEW_PRODUCT,
                            onClick = { type = NotificationType.NEW_PRODUCT },
                            label = { Text("🆕") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = type == NotificationType.DISCOUNT,
                            onClick = { type = NotificationType.DISCOUNT },
                            label = { Text("🔥") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = type == NotificationType.ARRIVAL,
                            onClick = { type = NotificationType.ARRIVAL },
                            label = { Text("📦") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = type == NotificationType.EVENT,
                            onClick = { type = NotificationType.EVENT },
                            label = { Text("🎉") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = type == NotificationType.INFO,
                            onClick = { type = NotificationType.INFO },
                            label = { Text("ℹ️") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val notification = Notification(
                        title = title,
                        content = content,
                        type = type,
                        createdBy = auth.currentUser?.uid ?: ""
                    )
                    onSave(notification)
                },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


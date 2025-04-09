package com.bav.labdispositivosmovilesbav.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.Icons as MaterialIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userRole: String,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showCameraAndAudioToast by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

        if (cameraGranted && audioGranted) {
            showCameraAndAudioToast = true
        } else {
            Toast.makeText(navController.context, "Permisos requeridos no concedidos", Toast.LENGTH_SHORT).show()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.logout_confirmation_title)) },
            text = { Text(stringResource(R.string.logout_confirmation_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            if (userRole.trim() == "Administrador") R.string.welcome_admin
                            else R.string.welcome_user
                        ),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = MaterialIcons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.logout),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título de bienvenida
            Text(
                text = stringResource(
                    if (userRole.trim() == "Administrador") R.string.welcome_message_admin
                    else R.string.welcome_message_user
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón para solicitar permisos
            ElevatedButton(
                onClick = {
                    val cameraPermission = ContextCompat.checkSelfPermission(navController.context, Manifest.permission.CAMERA)
                    val audioPermission = ContextCompat.checkSelfPermission(navController.context, Manifest.permission.RECORD_AUDIO)

                    if (cameraPermission == PackageManager.PERMISSION_GRANTED && audioPermission == PackageManager.PERMISSION_GRANTED) {
                        showCameraAndAudioToast = true
                    } else {
                        permissionLauncher.launch(
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = MaterialIcons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Text(
                        text = if (userRole.trim() == "Administrador") 
                            "¡Hola Administrador! Haz clic para comenzar"
                        else 
                            "¡Hola! Haz clic para comenzar"
                    )
                }
            }

            if (showCameraAndAudioToast) {
                Toast.makeText(navController.context, "Cámara y micrófono activados", Toast.LENGTH_SHORT).show()

                // Botones según el rol
                if (userRole.trim() == "Administrador") {
                    // Botón de Configuración
                    ElevatedButton(
                        onClick = { navController.navigate("configuracion") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = MaterialIcons.Default.Settings,
                                contentDescription = null
                            )
                            Text(stringResource(R.string.settings))
                        }
                    }

                    // Botón de Gestión de Usuarios
                    ElevatedButton(
                        onClick = { navController.navigate("users") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = MaterialIcons.Default.Group,
                                contentDescription = null
                            )
                            Text(stringResource(R.string.manage_users))
                        }
                    }
                } else {
                    // Botón de Chat para usuarios normales
                    ElevatedButton(
                        onClick = { navController.navigate("users") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = MaterialIcons.Default.Chat,
                                contentDescription = null
                            )
                            Text(stringResource(R.string.chat))
                        }
                    }
                }

                // Botón de Videollamada
                ElevatedButton(
                    onClick = {
                        val channelName = "canal123"
                        navController.navigate("video_call/$channelName")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = MaterialIcons.Default.VideoCall,
                            contentDescription = null
                        )
                        Text("Iniciar videollamada")
                    }
                }
            }
        }
    }
}
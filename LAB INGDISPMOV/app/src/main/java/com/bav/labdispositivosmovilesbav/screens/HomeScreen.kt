package com.bav.labdispositivosmovilesbav.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    var currentRole by remember { mutableStateOf(userRole) }
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

    // Lógica del diálogo de cierre de sesión
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
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(imageVector = MaterialIcons.Default.ExitToApp, contentDescription = stringResource(R.string.logout))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón para solicitar permisos
            Button(onClick = {
                val cameraPermission = ContextCompat.checkSelfPermission(navController.context, Manifest.permission.CAMERA)
                val audioPermission = ContextCompat.checkSelfPermission(navController.context, Manifest.permission.RECORD_AUDIO)

                if (cameraPermission == PackageManager.PERMISSION_GRANTED && audioPermission == PackageManager.PERMISSION_GRANTED) {
                    showCameraAndAudioToast = true
                } else {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    )
                }
            }) {
                Text("Unirse a sala")
            }

            if (showCameraAndAudioToast) {
                Toast.makeText(navController.context, "Cámara y micrófono activados", Toast.LENGTH_SHORT).show()
            }

            // Renderiza botones según el rol
            if (userRole.trim() == "Administrador") {
                Button(onClick = { navController.navigate("configuracion") }) {
                    Text(stringResource(R.string.settings))
                }
                Button(onClick = { navController.navigate("users") }) {
                    Text(stringResource(R.string.manage_users))
                }
            } else {
                Button(onClick = { navController.navigate("users") }) {
                    Text(stringResource(R.string.chat))
                }
            }
        }
    }
}
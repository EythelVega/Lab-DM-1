package com.bav.labdispositivosmovilesbav.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userRole: String,
    onLogout: () -> Unit
) {
    val isAdmin = remember(userRole) { userRole.trim() == "Administrador" }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Log.d("HomeScreen", "Iniciando HomeScreen con rol: $userRole, isAdmin: $isAdmin")

    // Dialog de confirmación
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.logout_confirmation_title)) },
            text = { Text(stringResource(R.string.logout_confirmation_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        try {
                            FirebaseAuth.getInstance().signOut()
                            Log.d("HomeScreen", "Sesión cerrada correctamente")
                            onLogout()
                        } catch (e: Exception) {
                            Log.e("HomeScreen", "Error al cerrar sesión: ${e.message}")
                        }
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
                            if (isAdmin) R.string.welcome_admin
                            else R.string.welcome_user
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.logout)
                        )
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
            // Añadir un texto para mostrar el rol actual (para debug)
            Text("Rol actual: $userRole")
            
            if (isAdmin) {
                Log.d("HomeScreen", "Renderizando botones de administrador")
                // Botones de Administrador
                Button(
                    onClick = { navController.navigate("configuracion") }
                ) {
                    Text(stringResource(R.string.settings))
                }
                Button(
                    onClick = { navController.navigate("users") }
                ) {
                    Text(stringResource(R.string.manage_users))
                }
            } else {
                Log.d("HomeScreen", "Renderizando botones de usuario normal")
                Button(
                    onClick = { navController.navigate("users") }
                ) {
                    Text(stringResource(R.string.chat))
                }
            }
        }
    }
} 
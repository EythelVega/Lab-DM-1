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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bav.labdispositivosmovilesbav.components.LanguageSelector
import com.bav.labdispositivosmovilesbav.components.LogoutDialog
import com.bav.labdispositivosmovilesbav.R
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userRole: String, // Se agrega el rol como parámetro
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    LanguageSelector()
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.logout)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == "Administrador") { // Botón flotante exclusivo para administradores
                FloatingActionButton(onClick = {
                    navController.navigate("new_chat")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo chat")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (userRole == "Administrador") {
                // Opciones exclusivas para administradores
                Button(onClick = {
                    navController.navigate("configuracion") // Navegar a configuración
                }) {
                    Text("Ir a Configuración")
                }
                Text("Eres un administrador.")
            } else {
                // Opciones para usuarios estándar
                Text("Eres un usuario estándar y no tienes permisos administrativos.")
            }

            Spacer(modifier = Modifier.height(16.dp))
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

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        navController = rememberNavController(),
        userRole = "Administrador", // Para probar como administrador
        onLogout = {}
    )
}
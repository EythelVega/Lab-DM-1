package com.bav.labdispositivosmovilesbav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(userRole: String, navController: NavController) {
    val idiomas = listOf("es", "en", "fr") // Idiomas disponibles
    var idiomaSeleccionado by remember { mutableStateOf("es") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pantalla de Configuración (Solo Administradores)",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Selecciona un idioma:")
            Spacer(modifier = Modifier.height(8.dp))

            // Botones para cada idioma disponible
            idiomas.forEach { languageCode ->
                Button(onClick = {
                    idiomaSeleccionado = languageCode
                    LanguageManager.setLocale(context, idiomaSeleccionado) // Cambia el idioma
                }) {
                    Text(text = languageCode.uppercase()) // Mostrar código de idioma
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para regresar al Home
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Volver al Home")
            }
        }
    }
}
package com.bav.labdispositivosmovilesbav.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.bav.labdispositivosmovilesbav.components.LanguageSelector
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(userRole: String, navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val currentLanguage by userPreferencesRepository.language.collectAsState(initial = "es")
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            LanguageSelector(
                userRole = userRole,
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    scope.launch {
                        userPreferencesRepository.setLanguage(language)
                        LanguageManager.setLocale(context, language)
                    }
                },
                userPreferencesRepository = userPreferencesRepository
            )

            Text(
                text = "Pantalla de Configuración (Solo Administradores)",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para regresar al Home
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Volver al Home")
            }
        }
    }
}
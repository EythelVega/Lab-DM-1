package com.bav.labdispositivosmovilesbav.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.bav.labdispositivosmovilesbav.R

@Composable
fun SplashScreen(
    navController: NavController,
    onNavigateToLogin: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            delay(3000)
            isLoading = false
            onNavigateToLogin()
        } catch (e: Exception) {
            Log.e("SplashScreen", "Error en navegación: ${e.message}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mi_logo_foreground),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier.size(200.dp)
        )
    }
} 
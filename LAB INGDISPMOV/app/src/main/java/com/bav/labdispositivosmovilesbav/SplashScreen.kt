package com.bav.labdispositivosmovilesbav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Retraso de 2 segundos antes de ir a la pantalla de login
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") { // Cambia "login" para ir a la pantalla de inicio
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)), // Color de fondo
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "KAYID",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

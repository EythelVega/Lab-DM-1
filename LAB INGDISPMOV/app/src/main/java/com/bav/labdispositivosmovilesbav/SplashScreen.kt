package com.bav.labdispositivosmovilesbav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Retraso antes de navegar a la pantalla de inicio
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") { // Cambia "login" si quieres ir a otra pantalla
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E181E)), // Personaliza el color de fondo si lo deseas
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Agregar el texto "KAYID"
            Text(
                text = "KAYID",
                color = Color.White,
                fontSize = 75.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(5.dp)) // Espacio entre el texto y la imagen
            // Mostrar la imagen del logo
            Image(
                painter = painterResource(id = R.drawable.mi_logo_foreground), // Asegúrate de que el nombre coincide
                contentDescription = "Logo de la app",
                modifier = Modifier.size(300.dp) // Ajusta el tamaño si es necesario
            )
        }
    }
}

package com.bav.labdispositivosmovilesbav
// Importaciones necesarias para que funcione el exito del registro
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SuccessScreen(
    onTimeComplete: () -> Unit // Callback que se ejecuta cuando finaliza la cuenta regresiva
) {
    var remainingSeconds by remember { mutableStateOf(10) } // Estado para la cuenta regresiva

    LaunchedEffect(Unit) {
        repeat(10) { // Bucle que disminuye el contador cada segundo
            delay(1000)
            remainingSeconds--
        }
        onTimeComplete() // Llamada a la función cuando termina la cuenta regresiva
    }

    // Contenedor principal con fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // Azul oscuro
                        Color(0xFF0D47A1)  // Azul más claro
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de éxito con animación de aparición
            AnimatedVisibility(
                visible = true,
                enter = fadeIn()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.Green,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de éxito
            Text(
                text = "¡Registro exitoso!\nPor favor, verifica tu correo electrónico para activar tu cuenta.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje con el tiempo restante para regresar
            Text(
                text = "Volviendo a la pantalla de registro en $remainingSeconds segundos",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Indicador de progreso circular con el tiempo restante
            CircularProgressIndicator(
                progress = remainingSeconds / 10f, // Progreso basado en el tiempo restante
                modifier = Modifier.size(60.dp),
                color = Color.White
            )
        }
    }
}
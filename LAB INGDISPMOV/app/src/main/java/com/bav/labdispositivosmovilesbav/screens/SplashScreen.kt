package com.bav.labdispositivosmovilesbav.screens

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    onNavigateToLogin: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    
    // Animación de logo flotante
    val infiniteTransition = rememberInfiniteTransition(label = "floatAnimation")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "floatY"
    )
    
    // Animación de escala para el logo
    val scaleAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "scaleAnimation"
    )
    
    // Animación de fade in
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        visible = true
        delay(3000)
        isLoading = false
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFF8C00) // Naranja que combina con el logo
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .offset(y = floatY.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo con animación flotante
            Image(
                painter = painterResource(id = R.drawable.logo_kamehouse),
                contentDescription = "Logo KameHouse Laguna",
                modifier = Modifier
                    .size(300.dp)
                    .scale(scaleAnimation)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Frase motivacional
            Text(
                text = "Tu colección de ensueño te espera",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                letterSpacing = 0.5.sp
            )
        }
        
        // Loading dots en la parte inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Cargando...",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            
            // Dots animados
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LoadingDot(delayMillis = 0)
                LoadingDot(delayMillis = 160)
                LoadingDot(delayMillis = 320)
            }
        }
        
        // Versión en la esquina inferior
        Text(
            text = "v1.0.0",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

@Composable
fun LoadingDot(delayMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dotAnimation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, delayMillis = delayMillis, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .scale(scale)
            .background(
                color = Color.White,
                shape = androidx.compose.foundation.shape.CircleShape
            )
    )
} 
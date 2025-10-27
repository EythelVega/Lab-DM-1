package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.R
import com.bav.labdispositivosmovilesbav.components.BottomNavigationBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userRole: String,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            HeaderBar(
                onNotificationClick = { /* TODO */ },
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "home", userRole = userRole)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeSection(userRole)
            
            QuickAccessGrid(userRole, navController)
        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                FirebaseAuth.getInstance().signOut()
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun HeaderBar(
    onNotificationClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF6B35),
                        Color(0xFF004E89)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
            Text(
                text = "Kame House Laguna",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(48.dp))
            
            Spacer(modifier = Modifier.weight(1f))
            
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeSection(userRole: String) {
    // Animación flotante para el título
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translateY"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = translateY.dp)
        ) {
            Text(
                text = if (userRole.trim() == "Administrador") {
                    "¡Bienvenido administrador!"
                } else {
                    "¡Bienvenido otaku!"
                },
                color = Color(0xFFFFD700),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (userRole.trim() == "Administrador") {
                    "Gestiona tu tienda con eficiencia"
                } else {
                    "Tu colección de ensueño te está esperando"
                },
                color = Color(0xFFF8F9FA),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickAccessGrid(userRole: String, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.heightIn(max = 400.dp)
    ) {
        item {
            QuickAccessCard(
                title = "Ver Catálogo",
                gradientColors = listOf(
                    Color(0xFF7209B7),
                    Color(0xFF004E89)
                ),
                icon = Icons.Default.Folder,
                onClick = { navController.navigate("catalog") }
            )
        }
        
        item {
            QuickAccessCard(
                title = if (userRole.trim() == "Administrador") "Notificaciones" else "Notificaciones",
                gradientColors = listOf(
                    Color(0xFFFF6B35),
                    Color(0xFFFFD700)
                ),
                icon = Icons.Default.Notifications,
                onClick = { 
                    if (userRole.trim() == "Administrador") {
                        navController.navigate("manage_notifications")
                    } else {
                        navController.navigate("notifications")
                    }
                }
            )
        }
        
        item {
            QuickAccessCard(
                title = if (userRole.trim() == "Administrador") "Facebook" else "Síguenos",
                gradientColors = listOf(
                    Color(0xFF4267B2),
                    Color(0xFF4267B2)
                ),
                icon = Icons.Default.Favorite,
                onClick = { navController.navigate("facebook") }
            )
        }
        
        item {
            QuickAccessCard(
                title = "Ayuda",
                gradientColors = listOf(
                    Color(0xFFE53E3E),
                    Color(0xFFC53030)
                ),
                icon = Icons.Default.Help,
                onClick = { navController.navigate("help") }
            )
        }
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    gradientColors: List<Color>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .scale(if (isPressed) 0.95f else 1f)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 2.dp else 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradientColors)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
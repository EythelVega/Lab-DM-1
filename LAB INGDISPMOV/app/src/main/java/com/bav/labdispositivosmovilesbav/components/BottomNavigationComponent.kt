package com.bav.labdispositivosmovilesbav.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String = "", userRole: String = "Usuario") {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color(0xFFF8F9FA)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home, 
                isActive = currentRoute == "home", 
                label = "Home",
                onClick = { navController.navigate("home") }
            )
            NavItem(
                icon = Icons.Default.Folder, 
                isActive = currentRoute == "catalog", 
                label = "Catálogo",
                onClick = { navController.navigate("catalog") }
            )
            NavItem(
                icon = Icons.Default.Notifications, 
                isActive = currentRoute == "notifications" || currentRoute == "manage_notifications", 
                label = "Notificaciones",
                onClick = { 
                    if (userRole.trim() == "Administrador") {
                        navController.navigate("manage_notifications")
                    } else {
                        navController.navigate("notifications")
                    }
                }
            )
            NavItem(
                icon = Icons.Default.Favorite, 
                isActive = currentRoute == "facebook", 
                label = "Facebook",
                onClick = { navController.navigate("facebook") }
            )
            NavItem(
                icon = Icons.Default.Help, 
                isActive = currentRoute == "help", 
                label = "Ayuda",
                onClick = { navController.navigate("help") }
            )
        }
    }
}

@Composable
fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    label: String,
    onClick: () -> Unit
) {
    val backgroundColor = if (isActive) Color(0xFFFFE5D9) else Color.Transparent
    val iconColor = if (isActive) Color(0xFFFF6B35) else Color(0xFF718096)
    val iconSize = if (isActive) 28.dp else 24.dp

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

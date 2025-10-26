package com.bav.labdispositivosmovilesbav.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.clickable { navController.navigateUp() }
                        )
                        Text(
                            "Ayuda",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE53E3E)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
        ) {
            // Logo y título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE53E3E),
                                Color(0xFFC53030)
                            )
                        )
                    )
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                color = Color.White,
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Help,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color(0xFFE53E3E)
                        )
                    }
                    
                    Text(
                        text = "Centro de Ayuda",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Descripción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "¿Necesitas ayuda?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        
                        Text(
                            text = "En Kame House Laguna queremos ayudarte. Descarga nuestro manual de usuario completo para aprender a usar todas las funcionalidades de la aplicación.",
                            fontSize = 16.sp,
                            color = Color(0xFF718096),
                            lineHeight = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF667eea),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "PDF gratuito y sin publicidad",
                                fontSize = 14.sp,
                                color = Color(0xFF667eea),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                
                // Botón de descarga
                Button(
                    onClick = {
                        downloadManual(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53E3E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Descargar Manual de Usuario",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Características del manual
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Contenido del Manual",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        
                        HelpFeatureItem(
                            icon = Icons.Default.Login,
                            title = "Inicio de sesión y registro",
                            description = "Aprende a crear tu cuenta y acceder a la aplicación"
                        )
                        
                        HelpFeatureItem(
                            icon = Icons.Default.Folder,
                            title = "Navegación del catálogo",
                            description = "Explora y filtra productos de manera sencilla"
                        )
                        
                        HelpFeatureItem(
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Panel de administrador",
                            description = "Gestión completa de productos e inventario"
                        )
                        
                        HelpFeatureItem(
                            icon = Icons.Default.Info,
                            title = "Soporte técnico",
                            description = "Solución de problemas comunes y preguntas frecuentes"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun HelpFeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF7FAFC),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFFE53E3E)
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF718096)
            )
        }
    }
}

fun downloadManual(context: Context) {
    try {
        // Leer el PDF desde assets
        val inputStream = context.assets.open("manual_usuario.pdf")
        
        // Crear carpeta de descargas si no existe
        val downloadsDir = context.getExternalFilesDir(null)
        val file = File(downloadsDir, "manual_usuario_kame_house_laguna.pdf")
        
        // Guardar el archivo
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        
        // Abrir el PDF usando FileProvider para Android 7+
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "com.bav.labdispositivosmovilesbav.fileprovider",
            file
        )
        
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
        
    } catch (e: Exception) {
        android.util.Log.e("HelpScreen", "Error descargando manual: ${e.message}")
        e.printStackTrace()
    }
}

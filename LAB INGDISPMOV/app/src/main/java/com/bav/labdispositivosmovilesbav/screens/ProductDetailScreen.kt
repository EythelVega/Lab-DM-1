package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bav.labdispositivosmovilesbav.components.BottomNavigationBar
import com.models.Product
import com.models.ProductStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    userRole: String = "Usuario"
) {
    val context = LocalContext.current
    val repository = remember { com.bav.labdispositivosmovilesbav.repository.ProductRepository() }
    
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(productId) {
        // Obtener producto por ID
        isLoading = true
        val products = repository.getAllProducts()
        product = products.find { it.id == productId }
        isLoading = false
    }
    
    if (isLoading || product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    val currentProduct = product!!
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                        Text(
                            currentProduct.name,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                        )
                    )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "product_detail", userRole = userRole)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(Color(0xFFF7FAFC)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    currentProduct.imageUrl.isNotEmpty() && currentProduct.imageUrl.startsWith("http") -> {
                        AsyncImage(
                            model = currentProduct.imageUrl,
                            contentDescription = currentProduct.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    currentProduct.imageUrl.isNotEmpty() -> {
                        val drawableId = context.resources.getIdentifier(
                            currentProduct.imageUrl,
                            "drawable",
                            context.packageName
                        )
                        if (drawableId != 0) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = drawableId),
                                contentDescription = currentProduct.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
            
            // Información del producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Estado del producto
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (currentProduct.status) {
                        ProductStatus.EN_STOCK -> Color(0xFFE6FFFA)
                        ProductStatus.PROXIMAMENTE -> Color(0xFFFED7AA)
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = when (currentProduct.status) {
                                ProductStatus.EN_STOCK -> "✅ En stock"
                                ProductStatus.PROXIMAMENTE -> "⏳ Próximamente"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (currentProduct.status) {
                                ProductStatus.EN_STOCK -> Color(0xFF38A169)
                                ProductStatus.PROXIMAMENTE -> Color(0xFFC05621)
                            }
                        )
                    }
                }
                
                // Nombre del producto
                Text(
                    text = currentProduct.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 32.sp
                )
                
                // Descripción del producto
                Text(
                    text = currentProduct.description,
                    fontSize = 16.sp,
                    color = Color(0xFF4A5568),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Badge de información adicional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF7FAFC)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("📦", fontSize = 24.sp)
                            Column {
                                Text(
                                    "Información del producto",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                                Text(
                                    text = when (currentProduct.status) {
                                        ProductStatus.EN_STOCK -> "Producto disponible en tienda"
                                        ProductStatus.PROXIMAMENTE -> "Disponible próximamente - Llegará pronto"
                                    },
                                    fontSize = 12.sp,
                                    color = Color(0xFF718096)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

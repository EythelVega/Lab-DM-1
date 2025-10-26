package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bav.labdispositivosmovilesbav.repository.ProductRepository
import com.models.Product
import com.models.ProductStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    userRole: String
) {
    val repository = remember { ProductRepository() }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf<ProductStatus?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    // Cargar productos
    LaunchedEffect(selectedFilter) {
        isLoading = true
        try {
            products = if (selectedFilter != null) {
                repository.getProductsByStatus(selectedFilter!!)
            } else {
                repository.getAllProducts()
            }
        } finally {
            isLoading = false
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Header con gradiente morado
        HeaderBar(
            title = "Catálogo de Figuras",
            onBackClick = { navController.navigateUp() },
            userRole = userRole,
            onManageProducts = {
                navController.navigate("manage_products")
            }
        )
        
        // Barra de filtros
        FilterBar(
            selectedFilter = selectedFilter,
            onFilterSelected = { filter ->
                selectedFilter = if (selectedFilter == filter) null else filter
            }
        )
        
        // Lista de productos
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos disponibles",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}

@Composable
fun HeaderBar(
    title: String,
    onBackClick: () -> Unit,
    userRole: String,
    onManageProducts: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2)
                    )
                )
            )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                // Solo mostrar botón de gestión para administradores
                if (userRole.trim() == "Administrador") {
                    IconButton(onClick = onManageProducts) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Gestionar productos",
                            tint = Color.White
                        )
                    }
                }
            }
            
            // Barra dorada inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF6B35),
                                Color(0xFFF7931E),
                                Color(0xFFFFD23F)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun FilterBar(
    selectedFilter: ProductStatus?,
    onFilterSelected: (ProductStatus) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        color = Color(0xFFF8F9FA)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                label = "En Stock",
                status = ProductStatus.EN_STOCK,
                isSelected = selectedFilter == ProductStatus.EN_STOCK,
                onSelected = { onFilterSelected(ProductStatus.EN_STOCK) },
                backgroundColor = Color(0xFF4CAF50),
                textColor = Color.White
            )
            
            FilterChip(
                label = "Próximamente",
                status = ProductStatus.PROXIMAMENTE,
                isSelected = selectedFilter == ProductStatus.PROXIMAMENTE,
                onSelected = { onFilterSelected(ProductStatus.PROXIMAMENTE) },
                backgroundColor = Color(0xFFFF9800),
                textColor = Color.White
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    status: ProductStatus,
    isSelected: Boolean,
    onSelected: () -> Unit,
    backgroundColor: Color,
    textColor: Color
) {
    FilterChip(
        onClick = onSelected,
        label = { Text(label, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium) },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = backgroundColor,
            containerColor = backgroundColor.copy(alpha = 0.7f),
            selectedLabelColor = textColor
        )
    )
}

@Composable
fun ProductCard(product: Product) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { /* TODO: Navegar a detalle */ },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen del producto
            when {
                product.imageUrl.isNotEmpty() && product.imageUrl.startsWith("http") -> {
                    // URL de Firebase Storage
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                product.imageUrl.isNotEmpty() -> {
                    // Recurso Drawable local
                    val drawableId = context.resources.getIdentifier(
                        product.imageUrl,
                        "drawable",
                        context.packageName
                    )
                    if (drawableId != 0) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF667eea),
                                            Color(0xFF764ba2)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📦",
                                fontSize = 32.sp
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📦",
                            fontSize = 32.sp
                        )
                    }
                }
            }
            
            // Contenido del producto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 40.dp),  // Espacio para el badge de estado
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    maxLines = 2
                )
                
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color(0xFF718096),
                    maxLines = 3,  // Aumentado de 2 a 3 líneas
                    lineHeight = 20.sp
                )
            }
            
            // Badge de estado
            StatusBadge(status = product.status)
        }
    }
}

@Composable
fun StatusBadge(status: ProductStatus) {
    val (text, color) = when (status) {
        ProductStatus.EN_STOCK -> "En Stock" to Color(0xFF38A169)
        ProductStatus.PROXIMAMENTE -> "Próximamente" to Color(0xFFC05621)
    }
    
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = when (status) {
            ProductStatus.EN_STOCK -> Color(0xFFE6FFFA)
            ProductStatus.PROXIMAMENTE -> Color(0xFFFED7AA)
        }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

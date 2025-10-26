package com.bav.labdispositivosmovilesbav.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bav.labdispositivosmovilesbav.repository.ProductRepository
import com.models.Product
import com.models.ProductStatus
import kotlinx.coroutines.launch
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductsScreen(
    navController: NavController
) {
    val repository = remember { ProductRepository() }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    // Cargar productos
    LaunchedEffect(Unit) {
        isLoading = true
        products = repository.getAllProducts()
        isLoading = false
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        TopAppBar(
            title = { Text("Gestionar Productos") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            },
            actions = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, "Agregar producto")
                }
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Text(
                        "No hay productos",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductManagementCard(
                        product = product,
                        onEdit = { editingProduct = it },
                        onDelete = {
                            scope.launch {
                                repository.deleteProduct(product.id)
                                products = repository.getAllProducts()
                            }
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo para agregar/editar producto
    if (showAddDialog || editingProduct != null) {
        AddEditProductDialog(
            product = editingProduct,
            onDismiss = {
                showAddDialog = false
                editingProduct = null
            },
            onSave = { savedProduct, imageBytes ->
                scope.launch {
                    try {
                        if (editingProduct != null) {
                            repository.updateProduct(savedProduct, imageBytes).fold(
                                onSuccess = {
                                    products = repository.getAllProducts()
                                    showAddDialog = false
                                    editingProduct = null
                                },
                                onFailure = { /* Manejo de error */ }
                            )
                        } else {
                            repository.addProduct(savedProduct, imageBytes).fold(
                                onSuccess = {
                                    products = repository.getAllProducts()
                                    showAddDialog = false
                                    editingProduct = null
                                },
                                onFailure = { /* Manejo de error */ }
                            )
                        }
                    } catch (e: Exception) {
                        // Manejo de error
                    }
                }
            }
        )
    }
}

@Composable
fun ProductManagementCard(
    product: Product,
    onEdit: (Product) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(product) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (product.status) {
                        ProductStatus.EN_STOCK -> Color(0xFFE6FFFA)
                        ProductStatus.PROXIMAMENTE -> Color(0xFFFED7AA)
                    }
                ) {
                    Text(
                        text = when (product.status) {
                            ProductStatus.EN_STOCK -> "En Stock"
                            ProductStatus.PROXIMAMENTE -> "Próximamente"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (product.status) {
                            ProductStatus.EN_STOCK -> Color(0xFF38A169)
                            ProductStatus.PROXIMAMENTE -> Color(0xFFC05621)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductDialog(
    product: Product?,
    onDismiss: () -> Unit,
    onSave: (Product, ByteArray?) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var status by remember { mutableStateOf(product?.status ?: ProductStatus.EN_STOCK) }
    var selectedDrawableName by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    
    // Lista de imágenes predefinidas disponibles
    val availableDrawables = listOf(
        "goku_imagaworks",  // Goku
        "batman_hottoy",  // Batman
        "naruto_anime_heroes"  // Naruto
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Agregar Producto" else "Editar Producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Selector de imagen
                Text("Imagen del producto", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableDrawables.forEach { drawableName ->
                        val drawableId = context.resources.getIdentifier(
                            drawableName,
                            "drawable",
                            context.packageName
                        )
                        if (drawableId != 0) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedDrawableName = drawableName },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedDrawableName == drawableName) 
                                        Color(0xFFE3F2FD) else Color.White
                                ),
                                border = if (selectedDrawableName == drawableName) 
                                    BorderStroke(2.dp, Color(0xFF2196F3)) else null
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = drawableName,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del producto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Selector de status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        selected = status == ProductStatus.EN_STOCK,
                        onClick = { status = ProductStatus.EN_STOCK },
                        label = { Text("En Stock") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = status == ProductStatus.PROXIMAMENTE,
                        onClick = { status = ProductStatus.PROXIMAMENTE },
                        label = { Text("Próximamente") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedProduct = product?.copy(
                        name = name,
                        description = description,
                        status = status
                    ) ?: Product(
                        name = name,
                        description = description,
                        status = status
                    )
                    // Guardar el nombre del drawable en lugar de bytes
                    val productWithImage = updatedProduct.copy(
                        imageUrl = selectedDrawableName ?: ""
                    )
                    onSave(productWithImage, null)
                },
                enabled = name.isNotBlank() && description.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

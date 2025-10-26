package com.bav.labdispositivosmovilesbav.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.models.Product
import com.models.ProductStatus
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = db.collection("products")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                val createdAt = doc.getTimestamp("createdAt")?.toDate()?.time ?: System.currentTimeMillis()
                val updatedAt = doc.getTimestamp("updatedAt")?.toDate()?.time ?: System.currentTimeMillis()
                
                Product(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    description = doc.getString("description") ?: "",
                    status = ProductStatus.valueOf(
                        doc.getString("status") ?: ProductStatus.EN_STOCK.name
                    ),
                    imageUrl = doc.getString("imageUrl") ?: "",
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error obteniendo productos: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun getProductsByStatus(status: ProductStatus): List<Product> {
        return try {
            val snapshot = db.collection("products")
                .whereEqualTo("status", status.name)
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                val createdAt = doc.getTimestamp("createdAt")?.toDate()?.time ?: System.currentTimeMillis()
                val updatedAt = doc.getTimestamp("updatedAt")?.toDate()?.time ?: System.currentTimeMillis()
                
                Product(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    description = doc.getString("description") ?: "",
                    status = ProductStatus.valueOf(
                        doc.getString("status") ?: ProductStatus.EN_STOCK.name
                    ),
                    imageUrl = doc.getString("imageUrl") ?: "",
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error obteniendo productos por status: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun addProduct(product: Product, imageBytes: ByteArray? = null): Result<String> {
        return try {
            var finalProduct = product
            
            // Subir imagen si existe
            if (imageBytes != null) {
                val imageUrl = uploadImage(product.name, imageBytes)
                finalProduct = product.copy(imageUrl = imageUrl)
            }
            
            // Preparar el mapa de datos con Timestamp
            val productMap = hashMapOf<String, Any>(
                "name" to (finalProduct.name),
                "description" to (finalProduct.description),
                "status" to (finalProduct.status.name),
                "imageUrl" to (finalProduct.imageUrl),
                "createdAt" to Timestamp.now(),
                "updatedAt" to Timestamp.now()
            )
            
            val documentRef = db.collection("products")
                .add(productMap)
                .await()
            
            Log.d("ProductRepository", "Producto añadido con ID: ${documentRef.id}")
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error añadiendo producto: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun updateProduct(product: Product, imageBytes: ByteArray? = null): Result<Unit> {
        return try {
            var finalProduct = product
            
            // Subir imagen si existe
            if (imageBytes != null) {
                val imageUrl = uploadImage(product.name, imageBytes)
                finalProduct = product.copy(imageUrl = imageUrl)
            }
            
            // Obtener el createdAt original del documento
            val existingDoc = db.collection("products")
                .document(product.id)
                .get()
                .await()
            
            val createdAt = existingDoc.getTimestamp("createdAt")
            
            // Preparar el mapa de datos con Timestamp
            val productMap = hashMapOf<String, Any>(
                "name" to (finalProduct.name),
                "description" to (finalProduct.description),
                "status" to (finalProduct.status.name),
                "imageUrl" to (finalProduct.imageUrl),
                "createdAt" to (createdAt ?: Timestamp.now()),
                "updatedAt" to Timestamp.now()
            )
            
            db.collection("products")
                .document(product.id)
                .set(productMap)
                .await()
            
            Log.d("ProductRepository", "Producto actualizado: ${product.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error actualizando producto: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            db.collection("products")
                .document(productId)
                .delete()
                .await()
            
            Log.d("ProductRepository", "Producto eliminado: $productId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error eliminando producto: ${e.message}")
            Result.failure(e)
        }
    }
    
    private suspend fun uploadImage(productName: String, imageBytes: ByteArray): String {
        val storageRef = storage.reference
        val imageRef = storageRef.child("products/${productName}_${System.currentTimeMillis()}.jpg")
        
        val uploadTask = imageRef.putBytes(imageBytes)
        uploadTask.await()
        
        return imageRef.downloadUrl.await().toString()
    }
}

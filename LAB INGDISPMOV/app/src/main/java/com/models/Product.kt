package com.models

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: ProductStatus = ProductStatus.EN_STOCK,
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ProductStatus {
    EN_STOCK,
    PROXIMAMENTE
}

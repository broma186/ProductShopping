package com.broma186.productshopping.data.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val currency: String,
    val description: String,
    val category: String,
    val available: Boolean,
    val inventory: Int,
    val rating: Double,
    val reviews: Int,
    val unit: String
)
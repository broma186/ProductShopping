package com.broma186.productshopping.presentation.model

data class Product(
    val id: Int,
    val name: String,
    val icon: String,
    val price: String,
    val description: String,
    val available: Boolean,
    val inventory: Int
)


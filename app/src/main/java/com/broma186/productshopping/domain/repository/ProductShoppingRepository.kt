package com.broma186.productshopping.domain.repository

import com.broma186.productshopping.data.model.ProductData

interface ProductShoppingRepository {
    suspend fun getProducts(): List<ProductData>
    suspend fun getProduct(id: Int): ProductData
    suspend fun updateCart(productId: Int, cartCount: Int): Boolean
    suspend fun getCartCount(productId: Int): Int
}
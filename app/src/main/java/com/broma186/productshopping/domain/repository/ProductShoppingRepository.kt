package com.broma186.productshopping.domain.repository

import com.broma186.productshopping.data.model.ProductsResponse

interface ProductShoppingRepository {
    suspend fun getProducts(): ProductsResponse
}
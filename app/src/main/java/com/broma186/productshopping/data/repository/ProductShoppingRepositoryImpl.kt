package com.broma186.productshopping.data.repository

import com.broma186.productshopping.data.api.ProductService
import com.broma186.productshopping.data.model.ProductsResponse
import com.broma186.productshopping.domain.repository.ProductShoppingRepository
import javax.inject.Inject

class ProductShoppingRepositoryImpl @Inject constructor(
    private val productService: ProductService
): ProductShoppingRepository {

    override suspend fun getProducts(): ProductsResponse {
        return productService.getProducts()
    }
}
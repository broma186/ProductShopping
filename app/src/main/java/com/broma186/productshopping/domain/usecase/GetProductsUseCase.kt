package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.data.model.ProductsResponse
import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class GetProductsUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(): ProductsResponse {
        return productShoppingRepository.getProducts()
    }
}
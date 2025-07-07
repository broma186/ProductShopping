package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class GetProductsUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(): List<ProductData> {
        return productShoppingRepository.getProducts()
    }
}
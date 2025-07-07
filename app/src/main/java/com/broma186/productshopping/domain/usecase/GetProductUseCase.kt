package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class GetProductUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(id: Int): ProductData {
        return productShoppingRepository.getProduct(id)
    }
}
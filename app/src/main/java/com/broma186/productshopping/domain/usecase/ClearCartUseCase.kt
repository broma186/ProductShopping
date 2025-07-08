package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class ClearCartUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(): Boolean {
        return productShoppingRepository.clearCart()
    }
}
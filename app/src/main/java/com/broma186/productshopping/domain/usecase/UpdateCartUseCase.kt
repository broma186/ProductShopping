package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class UpdateCartUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(productId: Int, cartCount: Int): Boolean {
        return productShoppingRepository.updateCart(productId, cartCount)
    }
}
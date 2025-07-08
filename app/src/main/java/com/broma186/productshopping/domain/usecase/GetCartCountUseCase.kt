package com.broma186.productshopping.domain.usecase

import com.broma186.productshopping.domain.repository.ProductShoppingRepository

class GetCartCountUseCase(private val productShoppingRepository: ProductShoppingRepository) {
    suspend operator fun invoke(id: Int): Int {
        return productShoppingRepository.getCartCount(id)
    }
}
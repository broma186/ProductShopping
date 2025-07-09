package com.broma186.productshopping.presentation.model

data class ProductsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val products: List<Product> = emptyList(),
    val product: Product? = null,
    val cartCount: Int? = null,
    val error: ErrorState? = null
)

sealed class ErrorState {
    object NoData : ErrorState()
    data class Fail(val message: String?) : ErrorState()
}
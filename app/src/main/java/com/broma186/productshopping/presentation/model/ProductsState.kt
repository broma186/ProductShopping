package com.broma186.productshopping.presentation.model

data class ProductsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
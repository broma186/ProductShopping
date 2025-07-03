package com.broma186.productshopping.presentation.navigation

sealed class Screen(val route: String) {
    object Products : Screen("productCollection")
}
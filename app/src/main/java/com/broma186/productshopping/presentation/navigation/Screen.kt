package com.broma186.productshopping.presentation.navigation

sealed class Screen(val route: String) {
    data object Products : Screen("productsHome")
    data object ProductDetail : Screen("productDetail/{productId}")
    data object ShoppingCart : Screen("shoppingCart")
}
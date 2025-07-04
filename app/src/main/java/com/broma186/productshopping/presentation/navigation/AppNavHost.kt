package com.broma186.productshopping.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Products.route) {
        navigation(
            startDestination = "products",
            route = Screen.Products.route
        ) {
            composable("products") {
               // ProductsScreen()
            }
        }
    }
}
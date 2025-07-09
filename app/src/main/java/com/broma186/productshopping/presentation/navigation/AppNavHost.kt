package com.broma186.productshopping.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.broma186.productshopping.presentation.screens.ProductDetailsScreen
import com.broma186.productshopping.presentation.screens.ProductsScreen
import com.broma186.productshopping.presentation.screens.ShoppingCartScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Products.route) {
        composable(Screen.Products.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "app://productshopping/productsHome"
            })
        ) {
            ProductsScreen(navController)
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "app://productshopping/productDetail/{productId}"
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailsScreen(navController, productId, navController::popBackStack)
        }
        composable(Screen.ShoppingCart.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "app://productshopping/shoppingCart"
            })
        ) {
            ShoppingCartScreen(navController::popBackStack)
        }
    }
}
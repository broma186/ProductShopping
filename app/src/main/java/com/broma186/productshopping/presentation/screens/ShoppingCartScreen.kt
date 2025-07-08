package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.broma186.productshopping.PriceFormatter
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.components.CartItemRow
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.presentation.model.ProductsState
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel
import com.broma186.productshopping.presentation.viewmodel.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen() {

    val viewModel: ShoppingCartViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
    }
    val uiState = viewModel.uiState.collectAsState()
    ShoppingCartScreenContent(uiState.value)
}

@Composable
fun ShoppingCartScreenContent(
    uiState: ProductsState
) {
    when {
        uiState.isLoading -> {
            LoadingScreen()
        }

        uiState.products.isNotEmpty() -> {
            SuccessScreen(uiState.products)
        }

        !uiState.error.isNullOrEmpty() -> {
            ErrorScreen(Modifier, uiState.error)
        }
    }
}

@Composable
fun SuccessScreen(
    products: List<Product>
) {
    Scaffold(
        Modifier
            .fillMaxSize(),
        topBar = {
            AppBar(title = stringResource(id = R.string.shopping_cart_title))
        }) { innerPadding ->
            val state = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                state = state) {
                items(products) {
                    CartItemRow(
                        name = it.name,
                        totalPrice = PriceFormatter.totalPrice(it.currency, it.doublePrice, it.cartCount),
                        icon = it.icon,
                        inventory = it.inventory,
                        cartCount = it.cartCount,
                        onQuantityChange = {

                        },
                        onDeleteConfirmed = {

                        }
                    )
                }
            }

    }
}
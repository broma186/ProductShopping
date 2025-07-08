package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

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
            SuccessScreen(uiState.products) {

            }
        }

        !uiState.error.isNullOrEmpty() -> {
            ErrorScreen(Modifier, uiState.error)
        }
    }
}

@Composable
fun SuccessScreen(
    products: List<Product>,
    onCountChange: suspend (productId: Int, cartCount: Int) -> Boolean,
) {
    Scaffold(
        Modifier
            .fillMaxSize(),
        topBar = {
            AppBar(title = stringResource(id = R.string.shopping_cart_title))
        }) { innerPadding ->
        val state = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            state = state
        ) {
            itemsIndexed(products) { index, product ->
                if (index != 0) {
                    HorizontalDivider(color = Color.LightGray)
                }
                CartItemRow(
                    name = product.name,
                    totalPrice = PriceFormatter.totalPrice(
                        product.currency,
                        product.doublePrice,
                        product.cartCount
                    ),
                    icon = product.icon,
                    inventory = product.inventory,
                    cartCount = product.cartCount,
                    onCountChange = { id, count ->
                        coroutineScope.launch {
                            onCountChange.invoke(id, count)
                        }
                    },
                    onDeleteConfirmed = {
                        //resetCount()
                    }
                )
            }
        }

    }
}
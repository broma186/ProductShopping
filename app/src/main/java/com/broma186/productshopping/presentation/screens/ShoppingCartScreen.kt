package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.broma186.productshopping.PriceFormatter
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.components.CartItemRow
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel
import com.broma186.productshopping.presentation.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.launch

@Composable
fun ShoppingCartScreen(
    onBackClick: () -> Unit
) {
    val viewModel: ShoppingCartViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
    }
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        Modifier
            .fillMaxSize(),
        topBar = {
            AppBar(title = stringResource(id = R.string.shopping_cart_title), onBackClick)
        }) { innerPadding ->
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }

            uiState.products.isNotEmpty() -> {
                ShoppingCartScreenContent(
                    Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    ),
                    uiState.products,
                    viewModel::updateCart,
                    viewModel::clearCart
                )
            }

            uiState.error != null -> {
                ErrorScreen(Modifier, uiState.error)
            }
        }
    }
}

@Composable
fun ShoppingCartScreenContent(
    modifier: Modifier,
    products: List<Product>,
    updateCart: suspend (productId: Int, cartCount: Int) -> Boolean,
    clearCart: () -> Unit
) {
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
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
                    onCountChange = { count ->
                        coroutineScope.launch {
                            updateCart.invoke(product.id, count)
                        }
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                val totalItemCount = products.sumOf { it.cartCount }
                val totalPrice = products.sumOf { it.doublePrice * it.cartCount }
                Text(
                    text = "Total Item Count: $totalItemCount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cart Total: ${
                        PriceFormatter.formatPrice(
                            totalPrice,
                            products.firstOrNull()?.currency ?: "NZD"
                        )
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {
                    clearCart.invoke()
                }
            ) {
                Text("Clear Cart")
            }
        }
    }
}

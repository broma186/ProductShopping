package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.components.ProductItem
import com.broma186.productshopping.presentation.viewmodel.ProductDetailsViewModel

@Composable
fun ProductDetailsScreen(
    productId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: ProductDetailsViewModel = hiltViewModel()
    LaunchedEffect(productId) {
        viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
    }
    val uiState by viewModel.uiState.collectAsState()
    ProductDetailsScreenContent(
        uiState,
        onBackClick
    )
}

@Composable
fun ProductDetailsScreenContent(
    uiState: ProductDetailsViewModel.ProductState,
    onBackClick: () -> Unit
) {
    when {
        uiState.isLoading -> {
            LoadingScreen()
        }

        uiState.product != null && !uiState.isLoading  -> {
            val product = uiState.product
            Scaffold(
                Modifier
                    .fillMaxSize(),
                topBar = {
                    AppBar(title = product.name, onBackClick)
                }
            ) { innerPadding ->
                ProductItem(
                    modifier = Modifier.padding(innerPadding),
                    name = uiState.product.name,
                    icon = uiState.product.icon,
                    imageSize = 300.dp,
                    price = uiState.product.price,
                    description = uiState.product.description
                )
            }
        }

        !uiState.errorMessage.isNullOrEmpty() -> {
            ErrorScreen(errorMessage = uiState.errorMessage)
        }
        else -> {
            ErrorScreen(errorMessage = "Failed to load product")
        }
    }
}
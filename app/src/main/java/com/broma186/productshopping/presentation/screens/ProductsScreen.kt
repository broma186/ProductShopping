package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.components.ProductItem
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel

@Composable
fun ProductsScreen(
    navController: NavController
) {
    val viewModel: ProductsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
    }
    ProductsScreenContent(
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        products = viewModel.uiState.collectAsState().value.products,
        isRefreshing = viewModel.uiState.collectAsState().value.isRefreshing,
        onRefresh = {
            viewModel.onIntent(ProductsViewModel.ProductsIntent.RefreshProducts)
        }
    )
}

@Composable
fun ProductsScreenContent(
    navController: NavController,
    uiState: ProductsViewModel.ProductsState,
    products: List<Product>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    when {
        uiState.isLoading || uiState.isRefreshing -> {
            LoadingScreen()
        }

        uiState.products.isNotEmpty() -> {
            SuccessScreen(navController, products, isRefreshing, onRefresh)
        }

        !uiState.error.isNullOrEmpty() -> {
            ErrorScreen(Modifier, uiState.error)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SuccessScreen(
    navController: NavController,
    products: List<Product>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val state = rememberPullRefreshState(isRefreshing, onRefresh)
    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(state),
        topBar = {
            AppBar(title = stringResource(id = R.string.toolbar_name))
        }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            val gridState = rememberLazyGridState()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductItem(Modifier.clickable(
                        onClick = {
                            navController.navigate("productDetail/${product.id}") {
                                launchSingleTop = true
                            }
                        }
                    ),
                        name = product.name,
                        icon = product.icon,
                        price = product.price
                    )
                }
            }
        }
    }
}


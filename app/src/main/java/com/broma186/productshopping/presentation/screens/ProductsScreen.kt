package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.components.ProductItem
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.presentation.model.ProductsState
import com.broma186.productshopping.presentation.navigation.Screen
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel

@Composable
fun ProductsScreen(
    navController: NavController
) {
    val viewModel: ProductsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
    }
    val uiState = viewModel.uiState.collectAsState()
    ProductsScreenContent(
        navController = navController,
        uiState = uiState.value,
        onRefresh = {
            viewModel.onIntent(ProductsViewModel.ProductsIntent.RefreshProducts)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsScreenContent(
    navController: NavController,
    uiState: ProductsState,
    onRefresh: () -> Unit
) {
    val state = rememberPullRefreshState(uiState.isRefreshing, onRefresh)
    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(state),
        topBar = {
            AppBar(title = stringResource(id = R.string.toolbar_name)) {
                navController.navigate(Screen.ShoppingCart.route)
            }
        }) { innerPadding ->
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }

            uiState.products.isNotEmpty() -> {
                SuccessScreen(Modifier.padding(innerPadding), state, navController, uiState.products, uiState.isRefreshing)
            }

            uiState.error != null -> {
                ErrorScreen(Modifier, uiState.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SuccessScreen(
    modifier: Modifier,
    state: PullRefreshState,
    navController: NavController,
    products: List<Product>,
    isRefreshing: Boolean
) {
        Box(modifier) {
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
                            }
                        }
                    ),
                        name = product.name,
                        icon = product.icon,
                        price = product.price
                    )
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isRefreshing,
                state = state
            )
        }
}


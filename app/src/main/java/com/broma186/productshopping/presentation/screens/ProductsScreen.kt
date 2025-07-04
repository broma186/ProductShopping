package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.presentation.components.ProductItem
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel

@Composable
fun ProductsScreen() {
    val viewModel: ProductsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductsViewModel.ProductIntent.FetchProducts)
    }
    ProductsScreenContent(products = viewModel.uiState.collectAsState().value.products)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreenContent(products: List<Product>) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.Green),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.toolbar_name)) })
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
                    ProductItem(product)
                }
            }
        }
    }
}
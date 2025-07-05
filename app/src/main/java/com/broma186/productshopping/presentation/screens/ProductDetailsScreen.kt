package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.broma186.productshopping.presentation.components.AppBar
import com.broma186.productshopping.presentation.model.Product

@Composable
fun ProductDetailsScreen(
    product: Product,
    onBackClick: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize(),
        topBar = {
            AppBar(title = product.name, onBackClick)
        }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {


        }
    }
}
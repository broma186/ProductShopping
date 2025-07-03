package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ProductsScreen() {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(Color.Green)) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text("Product screen")
        }
    }
}
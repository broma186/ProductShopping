package com.broma186.productshopping.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.broma186.productshopping.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    shoppingCartActionIcon: Boolean = true
    ) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (shoppingCartActionIcon) {
                IconButton(
                    onClick = {
                        // TODO: Open shopping cart screen.
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.shopping_cart_24),
                        tint = Color.Black,
                        contentDescription = "shopping cart"
                    )
                }
            }
        })
}

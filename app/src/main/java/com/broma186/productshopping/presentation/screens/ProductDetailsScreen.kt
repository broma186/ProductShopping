package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.broma186.productshopping.R
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

        uiState.product != null && !uiState.isLoading -> {
            val product = uiState.product
            Scaffold(
                Modifier
                    .fillMaxSize(),
                topBar = {
                    AppBar(title = product.name, onBackClick)
                }
            ) { innerPadding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    val count = remember { mutableIntStateOf(0) }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        ProductItem(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            ),
                            name = product.name,
                            icon = product.icon,
                            imageSize = 300.dp,
                            price = product.price,
                            description = product.description
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (count.intValue > 0) {
                                        count.intValue--
                                    }
                                },
                                enabled = count.intValue > 0
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    painter = painterResource(id = R.drawable.minus_24),
                                    contentDescription = "Decrement",
                                    tint = if (count.intValue > 0) Color.Blue else Color.LightGray
                                )
                            }
                            Text(
                                text = "${count.intValue}",
                                fontSize = 32.sp,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            )
                            IconButton(
                                onClick = {
                                    if (count.intValue < product.inventory) {
                                        count.intValue++
                                    }
                                },
                                enabled = count.intValue < product.inventory
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increment",
                                    tint = if (count.intValue < product.inventory) Color.Blue else Color.LightGray
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            // onAddToCart(count)
                        },
                        enabled = product.inventory > 0 && count.intValue > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Add to Cart")
                    }
                }
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

@Composable
fun QuantitySelector(
    modifier: Modifier,
    count: MutableIntState,
    inventory: Int
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    if (count.intValue > 0) {
                        count.intValue--
                    }
                },
                enabled = count.intValue > 0
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.minus_24),
                    contentDescription = "Decrement",
                    tint = if (count.intValue > 0) Color.Blue else Color.LightGray
                )
            }
            Text(
                text = "$count",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(
                onClick = {
                    if (count.intValue < inventory) {
                        count.intValue++
                    }
                },
                enabled = count.intValue < inventory
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increment",
                    tint = if (count.intValue < inventory) Color.Blue else Color.LightGray
                )
            }
        }
    }
}
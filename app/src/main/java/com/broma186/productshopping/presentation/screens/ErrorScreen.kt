package com.broma186.productshopping.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.model.ErrorState

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: ErrorState
) {
    val errorMessage = when (error) {
        is ErrorState.NoData -> "No content to display"
        is ErrorState.Fail -> error.message ?: "No content to display"
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (error) {
                is ErrorState.NoData ->  Icon(
                    painter = painterResource(id = R.drawable.shopping_cart_24),
                    contentDescription = stringResource(id = R.string.content_desc_error),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                is ErrorState.Fail ->  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(id = R.string.content_desc_error),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val errorMessage = when (error) {
                is ErrorState.NoData -> "No content to display"
                is ErrorState.Fail -> error.message ?: "No content to display"
            }
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}
package com.broma186.productshopping.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.broma186.productshopping.presentation.ProductIconHelper

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    icon: String,
    imageSize: Dp = 100.dp,
    name: String,
    price: String,
    description: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 4.dp, shape = RoundedCornerShape(16.dp), color = Color.LightGray)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id =  ProductIconHelper.fromId(icon).iconRes),
            contentDescription = name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(imageSize)
        )
        HorizontalDivider(color = Color.LightGray)
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = price,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
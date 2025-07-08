package com.broma186.productshopping.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.ProductIconHelper

@Composable
fun CartItemRow(
    name: String,
    totalPrice: String,
    icon: String,
    inventory: Int,
    cartCount: Int,
    onCountChange: (cartCount: Int) -> Unit
) {
    val count = remember { mutableIntStateOf(cartCount) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(cartCount) {
        count.intValue = cartCount
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Remove Item") },
            text = { Text("Are you sure you want to delete $name?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onCountChange(0) // deletes from shopping list
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = ProductIconHelper.fromId(icon).iconRes),
            contentDescription = name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = name,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,

                    )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = totalPrice,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (count.intValue > 0) {
                            count.intValue--
                            onCountChange(count.intValue)
                        }
                    },
                    enabled = count.intValue > 0
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.minus_24),
                        contentDescription = "Decrement",
                        tint = if (count.intValue > 0) Color.Blue else Color.LightGray
                    )
                }

                Text(
                    text = "${count.intValue}",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = {
                        if (count.intValue < inventory) {
                            count.intValue++
                            onCountChange(count.intValue)
                        }
                    },
                    enabled = count.intValue < inventory
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increment",
                        tint = if (count.intValue < inventory) Color.Blue else Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { showConfirmDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete item",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
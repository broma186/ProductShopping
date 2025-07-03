package com.broma186.productshopping.data.model

import com.broma186.productshopping.R
import com.broma186.productshopping.presentation.ProductIconHelper

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val currency: String,
    val description: String,
    val icon: String,
    val category: String,
    val available: Boolean,
    val inventory: Int,
    val rating: Double,
    val reviews: Int,
    val unit: String
) {
    fun toIcon(): Int {
        return when (icon) {
            ProductIconHelper.UNKNOWN.id -> R.drawable.unknown
            ProductIconHelper.FENCE.id -> R.drawable.fence
            ProductIconHelper.ELECTRIC_BOLT.id -> R.drawable.electric_bolt
            ProductIconHelper.VIEW_COLUMN.id -> R.drawable.view_column
            ProductIconHelper.HANDYMAN.id -> R.drawable.handyman
            ProductIconHelper.DOOR_OPEN.id -> R.drawable.door_open
            else -> {
                R.drawable.unknown
            }
        }
    }
}


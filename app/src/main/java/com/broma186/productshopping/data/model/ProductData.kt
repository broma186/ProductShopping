package com.broma186.productshopping.data.model

import com.broma186.productshopping.PriceFormatter
import com.broma186.productshopping.presentation.model.Product

data class ProductData(
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
)

fun ProductData.mapToUI(): Product {
    return Product(
            id = id,
            name = name,
            icon = icon,
            price = PriceFormatter.formatPrice(price, currency),
            description = description,
            available = available,
            inventory = inventory
        )

}
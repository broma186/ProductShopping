package com.broma186.productshopping.data.model

import com.broma186.productshopping.PriceFormatter
import com.broma186.productshopping.data.db.ProductEntity
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
    val unit: String,
    val cartCount: Int? = null
)

fun ProductData.mapToUI(): Product {
    return Product(
            id = id,
            name = name,
            icon = icon,
            price = PriceFormatter.formatPrice(price, currency),
            doublePrice = price,
            currency = currency,
            description = description,
            available = available,
            inventory = inventory,
            cartCount = cartCount ?: 0
        )
}

fun ProductData.mapToEntity(cartCount: Int = 0): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        icon = icon,
        price = price,
        currency = currency,
        description = description,
        category = category,
        available = available,
        inventory = inventory,
        rating = rating,
        reviews = reviews,
        unit = unit,
        cartCount = cartCount
    )
}
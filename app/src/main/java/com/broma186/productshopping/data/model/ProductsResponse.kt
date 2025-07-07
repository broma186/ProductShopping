package com.broma186.productshopping.data.model

import com.broma186.productshopping.data.db.ProductEntity

data class ProductsResponse(
    val products: List<ProductData>
)

fun ProductsResponse.mapToEntity(): List<ProductEntity> {
    return products.map {
        ProductEntity(
            id = it.id,
            name = it.name,
            price = it.price,
            currency = it.currency,
            description = it.description,
            icon = it.icon,
            category = it.category,
            available = it.available,
            inventory = it.inventory,
            rating = it.rating,
            reviews = it.reviews,
            unit = it.unit
        )
    }
}

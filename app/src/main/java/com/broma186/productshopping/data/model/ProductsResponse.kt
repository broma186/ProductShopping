package com.broma186.productshopping.data.model

import com.broma186.productshopping.PriceFormatter
import com.broma186.productshopping.presentation.model.Product

data class ProductsResponse(
    val products: List<ProductData>
)

fun ProductsResponse.mapToUI(): List<Product> {
    return products.map {
        Product(
            id = it.id,
            name = it.name,
            icon = it.icon,
            price = PriceFormatter.formatPrice(it.price, it.currency),
            description = it.description,
            available = it.available,
            inventory = it.inventory
        )
    }
}

package com.broma186.productshopping.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.broma186.productshopping.data.model.ProductData

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
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

fun ProductEntity.toDomainModel(): ProductData {
    return ProductData(
            id = id,
            name = name,
            price = price,
            currency = currency,
            description = description,
            icon = icon,
            category = category,
            available = available,
            inventory = inventory,
            rating = rating,
            reviews = reviews,
            unit = unit
        )
}
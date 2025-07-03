package com.broma186.productshopping.presentation

import com.broma186.productshopping.R

enum class ProductIconHelper(val id: String?, val iconRes: Int) {
    UNKNOWN("unknown", R.drawable.unknown),
    FENCE("fence", R.drawable.fence),
    ELECTRIC_BOLT("electric_bolt", R.drawable.electric_bolt),
    VIEW_COLUMN("view_column", R.drawable.view_column),
    HANDYMAN("handyman", R.drawable.handyman),
    DOOR_OPEN("door_open", R.drawable.door_open );

    companion object {
        fun fromId(id: String?): ProductIconHelper = entries.find { it.id == id } ?: UNKNOWN
    }
}


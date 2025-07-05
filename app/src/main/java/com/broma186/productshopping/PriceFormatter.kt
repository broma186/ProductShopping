package com.broma186.productshopping

import java.text.NumberFormat
import java.util.Currency

object PriceFormatter {

    fun formatPrice(price: Double, currencyCode: String): String {
        val format = NumberFormat.getInstance()
        format.currency = Currency.getInstance(currencyCode)
        return format.format(price)
    }
}
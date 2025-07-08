package com.broma186.productshopping

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object PriceFormatter {

    /*
    Uses US locale for symbol just to show $ instead of NZ$
     */
    fun formatPrice(price: Double, currencyCode: String): String {
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        val currency = Currency.getInstance(currencyCode)
        format.currency = currency
        return format.format(price)
    }

    fun totalPrice(currency: String, price: Double, cartCount: Int): String {
        return "Total: $currency ${"%.2f".format(price * cartCount)}"
    }
}
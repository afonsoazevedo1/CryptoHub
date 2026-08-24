package com.example.cryptohub.core.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a Double value into a USD currency string.
 * Example: 1234.56 -> $1,234.56
 */
fun Double.formatUSD(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return formatter.format(this)
        .replace(",", "TEMP")
        .replace(".", ",")
        .replace("TEMP", ".")
        .replace(" ", "")
}

package com.example.cryptohub.core.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a Double value into a USD currency string.
 * Example: 1234.56 -> $1,234.56
 */
fun Double.formatUSD(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}

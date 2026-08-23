package com.example.cryptohub.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val EXPECTED_DATE_PARTS = 3
private const val YEAR_LENGTH = 4

/**
 * Formats an ISO date string to a Brazilian date format (dd/MM/yyyy).
 * Example: "2018-04-26T00:00:00.000Z" -> "26/04/2018"
 */
fun String.formatToBrazilianDate(): String {
    val dateStr = this.trim()
    if (dateStr.isEmpty()) return this
    
    val inputFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd"
    )
    
    var result: String? = null
    for (format in inputFormats) {
        val parsed = tryParse(dateStr, format)
        if (parsed != null) {
            result = parsed
            break
        }
    }
    
    return result ?: formatManual(dateStr)
}

private fun tryParse(dateStr: String, format: String): String? {
    return try {
        val parser = SimpleDateFormat(format, Locale.US)
        if (format.contains("'Z'")) {
            parser.timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = parser.parse(dateStr)
        if (date != null) {
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            outputFormat.format(date)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

private fun formatManual(dateStr: String): String {
    return try {
        val parts = dateStr.split("T")[0].split("-")
        if (parts.size == EXPECTED_DATE_PARTS && parts[0].length == YEAR_LENGTH) {
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}

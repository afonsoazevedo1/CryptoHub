package com.example.cryptohub.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateExtensionsTest {

    @Test
    fun `formatToBrazilianDate should format ISO date correctly`() {
        val input = "2018-04-26T00:00:00.000Z"
        val expected = "26/04/2018"
        assertEquals(expected, input.formatToBrazilianDate())
    }

    @Test
    fun `formatToBrazilianDate should format YYYY-MM-DD correctly`() {
        val input = "2018-04-26"
        val expected = "26/04/2018"
        assertEquals(expected, input.formatToBrazilianDate())
    }

    @Test
    fun `formatToBrazilianDate should return original string if format is unknown`() {
        val input = "unknown-date"
        assertEquals(input, input.formatToBrazilianDate())
    }
}

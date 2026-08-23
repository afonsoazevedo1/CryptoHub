package com.example.cryptohub.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberExtensionsTest {

    @Test
    fun `formatUSD should format positive double correctly`() {
        val number = 1234.56
        val result = number.formatUSD()
        assertEquals("$1,234.56", result)
    }

    @Test
    fun `formatUSD should format large number correctly`() {
        val number = 1000000.0
        val result = number.formatUSD()
        assertEquals("$1,000,000.00", result)
    }

    @Test
    fun `formatUSD should format zero correctly`() {
        val number = 0.0
        val result = number.formatUSD()
        assertEquals("$0.00", result)
    }
}

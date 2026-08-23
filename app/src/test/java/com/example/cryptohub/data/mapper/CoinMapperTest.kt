package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.AssetQuoteDto
import com.example.cryptohub.data.remote.dto.CoinDto
import com.example.cryptohub.data.remote.dto.ExchangeAssetDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CoinMapperTest {

    @Test
    fun `toCoin should map correctly when all fields are present`() {
        val dto = ExchangeAssetDto(
            currency = CoinDto(
                id = 1,
                name = "Bitcoin",
                symbol = "BTC",
                priceUsd = 50000.0
            ),
            quote = null
        )

        val result = dto.toCoin()

        assertEquals(1, result.id)
        assertEquals("Bitcoin", result.name)
        assertEquals("BTC", result.symbol)
        assertEquals(50000.0, result.priceUsd!!, 0.0)
    }

    @Test
    fun `toCoin should use default values when id and symbol are null`() {
        val dto = ExchangeAssetDto(
            currency = CoinDto(
                id = null,
                name = "Unknown",
                symbol = null,
                priceUsd = null
            ),
            quote = null
        )

        val result = dto.toCoin()

        assertEquals(0, result.id)
        assertEquals("Unknown", result.name)
        assertEquals("", result.symbol)
        assertEquals(null, result.priceUsd)
    }

    @Test
    fun `toCoin should fallback to quote price when currency priceUsd is null`() {
        val dto = ExchangeAssetDto(
            currency = CoinDto(
                id = 1,
                name = "Bitcoin",
                symbol = "BTC",
                priceUsd = null
            ),
            quote = mapOf(
                "USD" to AssetQuoteDto(price = 45000.0)
            )
        )

        val result = dto.toCoin()

        assertEquals(45000.0, result.priceUsd!!, 0.0)
    }

    @Test
    fun `toCoinList should map list of DTOs to list of Coins`() {
        val dtos = listOf(
            ExchangeAssetDto(
                currency = CoinDto(id = 1, name = "BTC", symbol = "BTC"),
                quote = null
            ),
            ExchangeAssetDto(
                currency = CoinDto(id = 2, name = "ETH", symbol = "ETH"),
                quote = null
            )
        )

        val result = dtos.toCoinList()

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }
}

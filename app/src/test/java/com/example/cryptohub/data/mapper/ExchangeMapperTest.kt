package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.ExchangeDto
import com.example.cryptohub.data.remote.dto.ExchangeQuoteDto
import com.example.cryptohub.data.remote.dto.UrlsDto
import com.example.cryptohub.domain.models.Coin
import org.junit.Assert.assertEquals
import org.junit.Test

class ExchangeMapperTest {

    @Test
    fun `toExchangeListItem should map ExchangeDto to ExchangeListItem correctly`() {
        val dto = ExchangeDto(
            id = 1,
            name = "Binance",
            logo = "logo_url",
            dateLaunched = "2017-07-14",
            quote = mapOf("USD" to ExchangeQuoteDto(volume24h = 1000.0))
        )

        val result = dto.toExchangeListItem()

        assertEquals(1, result.id)
        assertEquals("Binance", result.name)
        assertEquals("logo_url", result.logo)
        assertEquals(1000.0, result.spotVolumeUsd, 0.0)
        assertEquals("2017-07-14", result.dateLaunched)
    }

    @Test
    fun `toExchangeListItem should fallback to firstHistoricalData if dateLaunched is null`() {
        val dto = ExchangeDto(
            id = 1,
            name = "Binance",
            firstHistoricalData = "2017-07-14",
            dateLaunched = null
        )

        val result = dto.toExchangeListItem()

        assertEquals("2017-07-14", result.dateLaunched)
    }

    @Test
    fun `toExchangeListItem should use default logo when logo is null`() {
        val dto = ExchangeDto(
            id = 1,
            name = "Binance",
            logo = null,
            volume24h = null,
            spotVolumeUsd = 2000.0
        )

        val result = dto.toExchangeListItem()

        assertEquals("https://s2.coinmarketcap.com/static/img/exchanges/64x64/1.png", result.logo)
        assertEquals(2000.0, result.spotVolumeUsd, 0.0)
    }

    @Test
    fun `toExchangeDetail should map ExchangeDto to ExchangeDetail correctly`() {
        val dto = ExchangeDto(
            id = 1,
            name = "Binance",
            logo = "logo_url",
            description = "Description",
            urls = UrlsDto(website = listOf("website_url")),
            makerFee = 0.1,
            takerFee = 0.2,
            spotVolumeUsd = 5000000.0,
            dateLaunched = "2017-07-14"
        )
        val coins = listOf(Coin(1, "Bitcoin", "BTC", 50000.0))

        val result = dto.toExchangeDetail(coins)

        assertEquals(1, result.id)
        assertEquals("Binance", result.name)
        assertEquals("logo_url", result.logo)
        assertEquals("Description", result.description)
        assertEquals("website_url", result.website)
        assertEquals(0.1, result.makerFee!!, 0.0)
        assertEquals(0.2, result.takerFee!!, 0.0)
        assertEquals(5000000.0, result.spotVolumeUsd!!, 0.0)
        assertEquals("2017-07-14", result.dateLaunched)
        assertEquals(coins, result.currencies)
    }
}

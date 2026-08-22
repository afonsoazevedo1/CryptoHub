package com.example.cryptohub.data.remote

import com.example.cryptohub.data.remote.dto.CoinDto
import com.example.cryptohub.data.remote.dto.CoinListResponse
import com.example.cryptohub.data.remote.dto.ExchangeDetailResponse
import com.example.cryptohub.data.remote.dto.ExchangeDto
import com.example.cryptohub.data.remote.dto.ExchangeListResponse
import com.example.cryptohub.data.remote.dto.UrlsDto
import kotlinx.coroutines.delay

object FakeRemoteDataSource {
    private const val SIMULATED_DELAY_MS = 2000L // 2 segundos pra aparecer shimmer

    suspend fun getExchangesList(): ExchangeListResponse {
        delay(SIMULATED_DELAY_MS)
        return ExchangeListResponse(
            data = mapOf(
                "1" to ExchangeDto(
                    id = 1,
                    name = "Binance",
                    logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/270.png",
                    spotVolumeUsd = 14250000000.0,
                    dateLaunched = "2017-07-14",
                    description = "Binance is a cryptocurrency exchange platform.",
                    urls = UrlsDto(website = listOf("https://www.binance.com")),
                    makerFee = 0.1,
                    takerFee = 0.1
                ),
                "2" to ExchangeDto(
                    id = 2,
                    name = "Coinbase",
                    logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/89.png",
                    spotVolumeUsd = 1050000000.0,
                    dateLaunched = "2012-06-01",
                    description = "Coinbase is a digital asset exchange.",
                    urls = UrlsDto(website = listOf("https://www.coinbase.com")),
                    makerFee = 0.5,
                    takerFee = 0.6
                ),
                "3" to ExchangeDto(
                    id = 3,
                    name = "Kraken",
                    logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/24.png",
                    spotVolumeUsd = 750000000.0,
                    dateLaunched = "2011-07-28",
                    description = "Kraken is a Bitcoin and cryptocurrency exchange.",
                    urls = UrlsDto(website = listOf("https://www.kraken.com")),
                    makerFee = 0.16,
                    takerFee = 0.26
                )
            )
        )
    }

    suspend fun getExchangeDetail(exchangeId: Int): ExchangeDetailResponse {
        delay(SIMULATED_DELAY_MS)
        val exchange = when (exchangeId) {
            1 -> ExchangeDto(
                id = 1,
                name = "Binance",
                logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/270.png",
                spotVolumeUsd = 14250000000.0,
                dateLaunched = "2017-07-14",
                description = "Binance is one of the largest cryptocurrency exchange platforms by trading volume.",
                urls = UrlsDto(website = listOf("https://www.binance.com")),
                makerFee = 0.1,
                takerFee = 0.1
            )
            2 -> ExchangeDto(
                id = 2,
                name = "Coinbase",
                logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/89.png",
                spotVolumeUsd = 1050000000.0,
                dateLaunched = "2012-06-01",
                description = "Coinbase is a leading digital asset exchange platform.",
                urls = UrlsDto(website = listOf("https://www.coinbase.com")),
                makerFee = 0.5,
                takerFee = 0.6
            )
            else -> ExchangeDto(
                id = 3,
                name = "Kraken",
                logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/24.png",
                spotVolumeUsd = 750000000.0,
                dateLaunched = "2011-07-28",
                description = "Kraken is a Bitcoin and cryptocurrency exchange founded in 2011.",
                urls = UrlsDto(website = listOf("https://www.kraken.com")),
                makerFee = 0.16,
                takerFee = 0.26
            )
        }
        return ExchangeDetailResponse(data = exchange)
    }

    suspend fun getExchangeCoins(exchangeId: Int): CoinListResponse {
        delay(SIMULATED_DELAY_MS)
        return CoinListResponse(
            data = listOf(
                CoinDto(id = 1, name = "Bitcoin", symbol = "BTC", priceUsd = 45000.0),
                CoinDto(id = 2, name = "Ethereum", symbol = "ETH", priceUsd = 2500.0),
                CoinDto(id = 3, name = "Cardano", symbol = "ADA", priceUsd = 0.98),
                CoinDto(id = 4, name = "Solana", symbol = "SOL", priceUsd = 180.0),
                CoinDto(id = 5, name = "Ripple", symbol = "XRP", priceUsd = 2.50)
            )
        )
    }
}
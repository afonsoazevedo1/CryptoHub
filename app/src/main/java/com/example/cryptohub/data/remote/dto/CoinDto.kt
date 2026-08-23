package com.example.cryptohub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("symbol")
    val symbol: String? = null,
    @SerialName("price_usd")
    val priceUsd: Double? = null
)

@Serializable
data class AssetQuoteDto(
    @SerialName("price")
    val price: Double? = null
)

@Serializable
data class ExchangeAssetDto(
    @SerialName("currency")
    val currency: CoinDto,
    @SerialName("quote")
    val quote: Map<String, AssetQuoteDto>? = null
)

@Serializable
data class CoinListResponse(
    @SerialName("data")
    val data: List<ExchangeAssetDto>? = emptyList()
)

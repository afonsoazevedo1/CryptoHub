package com.example.cryptohub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("price_usd")
    val priceUsd: Double
)

@Serializable
data class CoinListResponse(
    @SerialName("data")
    val data: List<CoinDto>
)
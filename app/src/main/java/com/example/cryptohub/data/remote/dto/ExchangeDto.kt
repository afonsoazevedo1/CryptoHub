package com.example.cryptohub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("logo")
    val logo: String? = null,
    @SerialName("volume_24h")
    val volume24h: Double? = null,
    @SerialName("spot_volume_usd")
    val spotVolumeUsd: Double? = null,
    @SerialName("first_historical_data")
    val firstHistoricalData: String? = null,
    @SerialName("date_launched")
    val dateLaunched: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("urls")
    val urls: UrlsDto? = null,
    @SerialName("maker_fee")
    val makerFee: Double? = null,
    @SerialName("taker_fee")
    val takerFee: Double? = null,
    @SerialName("quote")
    val quote: Map<String, ExchangeQuoteDto>? = null
)

@Serializable
data class ExchangeQuoteDto(
    @SerialName("volume_24h")
    val volume24h: Double? = null,
    @SerialName("volume_24h_adjusted")
    val volume24hAdjusted: Double? = null
)

@Serializable
data class UrlsDto(
    @SerialName("website")
    val website: List<String>? = null
)

@Serializable
data class ExchangeListResponse(
    @SerialName("data")
    val data: List<ExchangeDto>
)

@Serializable
data class ExchangeDetailResponse(
    @SerialName("data")
    val data: Map<String, ExchangeDto>
)

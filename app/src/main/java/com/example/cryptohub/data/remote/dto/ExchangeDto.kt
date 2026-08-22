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
    @SerialName("spot_volume_usd")
    val spotVolumeUsd: Double,
    @SerialName("date_launched")
    val dateLaunched: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("urls")
    val urls: UrlsDto? = null,
    @SerialName("maker_fee")
    val makerFee: Double? = null,
    @SerialName("taker_fee")
    val takerFee: Double? = null
)

@Serializable
data class UrlsDto(
    @SerialName("website")
    val website: List<String>? = null
)

@Serializable
data class ExchangeListResponse(
    @SerialName("data")
    val data: Map<String, ExchangeDto>
)

@Serializable
data class ExchangeDetailResponse(
    @SerialName("data")
    val data: ExchangeDto
)
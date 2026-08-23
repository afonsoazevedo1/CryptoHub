package com.example.cryptohub.data.remote.api

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Resource("/v1/exchange/map")
data class ExchangeMapResource(
    @SerialName("listing_status") val listingStatus: String = "active",
    val start: Int = 1,
    val limit: Int = 20
)

@Serializable
@Resource("/v1/exchange/info")
data class ExchangeInfoResource(
    val id: Int
)

@Serializable
@Resource("/v1/exchange/assets")
data class ExchangeAssetsResource(
    val id: Int
)

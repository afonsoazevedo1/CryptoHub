package com.example.cryptohub.data.remote.api

import com.example.cryptohub.data.remote.dto.ExchangeDetailResponse
import com.example.cryptohub.data.remote.dto.ExchangeListResponse
import com.example.cryptohub.data.remote.dto.CoinListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinMarketCapApi {
    @GET("v1/exchange/info")
    suspend fun getExchanges(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("id") ids: String? = null
    ): ExchangeListResponse

    @GET("v1/exchange/info")
    suspend fun getExchangeDetail(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("id") exchangeId: Int
    ): ExchangeDetailResponse

    @GET("v1/exchange/assets")
    suspend fun getExchangeCoins(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("exchange_id") exchangeId: Int
    ): CoinListResponse
}
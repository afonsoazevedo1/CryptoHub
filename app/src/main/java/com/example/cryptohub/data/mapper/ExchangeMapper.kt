package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.ExchangeDto
import com.example.cryptohub.domain.models.Coin
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem

fun ExchangeDto.toExchangeListItem(): ExchangeListItem {
    val volume = spotVolumeUsd 
        ?: quote?.get("USD")?.volume24h 
        ?: volume24h 
        ?: 0.0
        
    return ExchangeListItem(
        id = id,
        name = name,
        logo = logo ?: "https://s2.coinmarketcap.com/static/img/exchanges/64x64/$id.png",
        spotVolumeUsd = volume,
        dateLaunched = dateLaunched ?: firstHistoricalData
    )
}

fun ExchangeDto.toExchangeDetail(coins: List<Coin> = emptyList()): ExchangeDetail {
    return ExchangeDetail(
        id = id,
        name = name,
        logo = logo ?: "https://s2.coinmarketcap.com/static/img/exchanges/64x64/$id.png",
        description = description,
        website = urls?.website?.firstOrNull(),
        makerFee = makerFee,
        takerFee = takerFee,
        spotVolumeUsd = spotVolumeUsd ?: quote?.get("USD")?.volume24h ?: volume24h,
        dateLaunched = dateLaunched ?: firstHistoricalData,
        currencies = coins
    )
}

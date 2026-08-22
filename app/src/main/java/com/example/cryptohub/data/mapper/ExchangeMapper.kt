package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.ExchangeDto
import com.example.cryptohub.domain.models.Coin
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem

fun ExchangeDto.toExchangeListItem(): ExchangeListItem {
    return ExchangeListItem(
        id = id,
        name = name,
        logo = logo,
        spotVolumeUsd = spotVolumeUsd,
        dateLaunched = dateLaunched
    )
}

fun ExchangeDto.toExchangeDetail(coins: List<Coin> = emptyList()): ExchangeDetail {
    return ExchangeDetail(
        id = id,
        name = name,
        logo = logo,
        description = description,
        website = urls?.website?.firstOrNull(),
        makerFee = makerFee,
        takerFee = takerFee,
        dateLaunched = dateLaunched,
        currencies = coins
    )
}
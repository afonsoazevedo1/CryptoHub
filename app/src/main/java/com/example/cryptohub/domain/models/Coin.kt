package com.example.cryptohub.domain.models

data class Coin(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceUsd: Double
)

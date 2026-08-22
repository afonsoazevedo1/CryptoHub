package com.example.cryptohub.domain.models

data class ExchangeListItem(
    val id: Int,
    val name: String,
    val logo: String?,
    val spotVolumeUsd: Double,
    val dateLaunched: String?
)

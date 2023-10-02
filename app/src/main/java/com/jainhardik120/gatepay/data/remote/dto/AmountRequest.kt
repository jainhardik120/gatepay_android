package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AmountRequest(
    val amount: Double
)
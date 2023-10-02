package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val transactionId: String,
    val orderId: String,
    val amount: Double
)



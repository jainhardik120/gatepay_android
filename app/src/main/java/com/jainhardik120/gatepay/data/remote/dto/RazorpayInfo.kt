package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RazorpayInfo(
    @SerialName("razorpay_order_id")
    val razorpayOrderId: String,
    @SerialName("razorpay_payment_id")
    val razorpayPaymentId: String,
    @SerialName("razorpay_signature")
    val razorpaySignature: String
)
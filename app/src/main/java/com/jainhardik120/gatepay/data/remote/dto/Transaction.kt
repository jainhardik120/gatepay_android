package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("transactionid") val transactionId: String,
    @SerialName("date") val date: String,
    @SerialName("transactiontype") val transactionType: String,
    @SerialName("status") val status: String,
    @SerialName("endbalance") val endBalance: String,
    @SerialName("amount") val amount: String,
    @SerialName("tollorparkingname") val tollOrParkingName: String?,
    @SerialName("location") val location: String?
)
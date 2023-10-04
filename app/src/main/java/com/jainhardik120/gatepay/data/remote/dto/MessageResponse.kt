package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("msg")
    val message: String
)
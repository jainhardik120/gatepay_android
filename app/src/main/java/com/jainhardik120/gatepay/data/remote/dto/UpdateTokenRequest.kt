package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenRequest(
    val firebaseToken: String
)
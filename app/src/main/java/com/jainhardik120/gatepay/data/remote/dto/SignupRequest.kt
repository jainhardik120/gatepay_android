package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    @SerialName("Email")
    val email: String,
    @SerialName("Password")
    val password: String,
    @SerialName("Name")
    val name: String
)



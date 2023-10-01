package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    @SerialName("Email")
    val email: String,
    @SerialName("Name")
    val name: String?,
    val isNewUser: Boolean,
    val token: String,
    val userID: String
)


package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    @SerialName("id")
    val id: String? = null,
    @SerialName("userid")
    val userId: String? = null,
    @SerialName("type")
    val type: String,
    @SerialName("vehicleno")
    val vehicleNo: String,
    @SerialName("manufacturer")
    val manufacturer: String,
    @SerialName("model")
    val model: String,
    @SerialName("color")
    val color: String
)
package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TollGateEntry(
    val color: String,
    val entryid: String,
    val entrytimestamp: String,
    val locationcoordinates: String,
    val manufacturer: String,
    val model: String,
    val tollgatename: String,
    val vehicleid: String,
    val vehicleno: String
)
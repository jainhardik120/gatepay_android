package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParkingEntryX(
    val charges: String,
    val color: String,
    val entryexitid: String,
    val entrytimestamp: String,
    val exittimestamp: String,
    val manufacturer: String,
    val model: String,
    val parkinglotid: String,
    val parkinglotname: String,
    val parkingspaceid: String,
    val transactionid: String,
    val vehicleid: String,
    val vehicleno: String
)
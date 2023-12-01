package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TollGateEntryX(
    val amount: String,
    val color: String,
    val entryid: String,
    val entryname: String,
    val entrytimestamp: String,
    val exitname: String,
    val exittimestamp: String,
    val manufacturer: String,
    val model: String,
    val tollgatename: String,
    val transactionid: String,
    val vehicleid: String,
    val vehicleno: String
)
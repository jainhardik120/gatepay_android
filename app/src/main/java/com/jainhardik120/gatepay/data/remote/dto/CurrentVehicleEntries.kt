package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CurrentVehicleEntries(
    val parkingEntries: List<ParkingEntry>,
    val tollGateEntries: List<TollGateEntry>
)
package com.jainhardik120.gatepay.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PastVehicleEntries(
    val parkingEntries: List<ParkingEntryX>,
    val tollGateEntries: List<TollGateEntryX>
)
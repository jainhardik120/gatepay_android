package com.jainhardik120.gatepay.ui.presentation.screens.home

import com.jainhardik120.gatepay.data.remote.dto.ParkingEntry
import com.jainhardik120.gatepay.data.remote.dto.TollGateEntry

data class HomeState(
    val userBalance: Double = 0.0,
    val parkingEntries: List<ParkingEntry> = emptyList(),
    val tollEntries: List<TollGateEntry> = emptyList()
)
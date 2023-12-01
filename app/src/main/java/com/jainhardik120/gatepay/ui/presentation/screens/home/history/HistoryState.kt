package com.jainhardik120.gatepay.ui.presentation.screens.home.history

import com.jainhardik120.gatepay.data.remote.dto.ParkingEntryX
import com.jainhardik120.gatepay.data.remote.dto.TollGateEntryX

data class HistoryState(
    val tollGateEntries: List<TollGateEntryX> = emptyList(),
    val parkingEntries: List<ParkingEntryX> = emptyList(),
)
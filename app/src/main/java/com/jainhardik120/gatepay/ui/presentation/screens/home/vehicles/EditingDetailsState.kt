package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import com.jainhardik120.gatepay.data.remote.dto.Vehicle

data class EditingDetailsState(
    val type: String = "",
    val vehicleNo: String = "",
    val manufacturer: String = "",
    val model: String = "",
    val color: String = "",
    val vehicles: List<Vehicle> = emptyList(),
    val isVehicleUpdate: Boolean = false,
    val updateVehicleId: String = ""
)
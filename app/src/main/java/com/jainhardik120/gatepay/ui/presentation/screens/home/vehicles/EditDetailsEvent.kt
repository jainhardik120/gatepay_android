package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import com.jainhardik120.gatepay.data.remote.dto.Vehicle

sealed class EditDetailsEvent {
    data class TypeChange(val newType: String) : EditDetailsEvent()
    data class VehicleNoChange(val newVehicleNo: String) : EditDetailsEvent()
    data class ManufacturerChange(val newManufacturer: String) : EditDetailsEvent()
    data class ModelChange(val newModel: String) : EditDetailsEvent()
    data class ColorChange(val newColor: String) : EditDetailsEvent()
    data class EditVehicleEvent(val vehicle: Vehicle) : EditDetailsEvent()
    data class DeleteVehicleEvent(val vehicle: Vehicle) : EditDetailsEvent()
    data object SaveButtonClick : EditDetailsEvent()
    data object AddVehicleFabClick : EditDetailsEvent()
}
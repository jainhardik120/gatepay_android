package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.Vehicle
import com.jainhardik120.gatepay.ui.BaseViewModel
import com.jainhardik120.gatepay.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(private val api: GatepayAPI) : BaseViewModel() {

    private var _state = mutableStateOf(EditingDetailsState())
    val state: State<EditingDetailsState> = _state


    init {
        refreshList()
    }

    private fun refreshList() {
        makeApiCall({ api.listVehicles() }) { arrayVehicles ->
            _state.value = _state.value.copy(vehicles = arrayVehicles)
        }
    }

    private fun areAllFieldsNonEmpty(state: EditingDetailsState): Boolean {
        return state.type.isNotBlank() &&
                state.vehicleNo.isNotBlank() &&
                state.manufacturer.isNotBlank() &&
                state.model.isNotBlank() &&
                state.color.isNotBlank()
    }

    fun handleEditDetailsEvent(event: EditDetailsEvent) {
        when (event) {
            is EditDetailsEvent.ColorChange -> {
                _state.value = _state.value.copy(color = event.newColor)
            }

            is EditDetailsEvent.ManufacturerChange -> {
                _state.value = _state.value.copy(manufacturer = event.newManufacturer)
            }

            is EditDetailsEvent.ModelChange -> {
                _state.value = _state.value.copy(model = event.newModel)
            }

            EditDetailsEvent.SaveButtonClick -> {
                if (_state.value.isVehicleUpdate) {
                    if (areAllFieldsNonEmpty(_state.value) && _state.value.updateVehicleId.isNotEmpty()) {
                        makeApiCall({
                            api.editVehicle(
                                Vehicle(
                                    type = _state.value.type,
                                    vehicleNo = _state.value.vehicleNo,
                                    manufacturer = _state.value.manufacturer,
                                    model = _state.value.model,
                                    color = _state.value.color
                                ), _state.value.updateVehicleId
                            )
                        }) {
                            _state.value =
                                _state.value.copy(vehicles = _state.value.vehicles.filter { it2 ->
                                    it2.id != it.id
                                } + it)
                            sendUiEvent(UiEvent.NavigateBack)
                        }
                    } else {
                        sendUiEvent(UiEvent.ShowToast("All fields are necessary"))
                    }
                } else {
                    if (areAllFieldsNonEmpty(_state.value)) {
                        makeApiCall({
                            api.addVehicle(
                                Vehicle(
                                    type = _state.value.type,
                                    vehicleNo = _state.value.vehicleNo,
                                    manufacturer = _state.value.manufacturer,
                                    model = _state.value.model,
                                    color = _state.value.color
                                )
                            )
                        }) {
                            _state.value = _state.value.copy(vehicles = _state.value.vehicles + it)
                            sendUiEvent(UiEvent.NavigateBack)
                        }
                    } else {
                        sendUiEvent(UiEvent.ShowToast("All fields are necessary"))
                    }
                }

            }

            is EditDetailsEvent.TypeChange -> {
                _state.value = _state.value.copy(type = event.newType)
            }

            is EditDetailsEvent.VehicleNoChange -> {
                _state.value = _state.value.copy(vehicleNo = event.newVehicleNo)
            }

            EditDetailsEvent.AddVehicleFabClick -> {
                _state.value = _state.value.copy(isVehicleUpdate = false, updateVehicleId = "")
                sendUiEvent(UiEvent.Navigate("create"))
            }

            is EditDetailsEvent.EditVehicleEvent -> {
                _state.value = _state.value.copy(
                    type = event.vehicle.type,
                    vehicleNo = event.vehicle.vehicleNo,
                    manufacturer = event.vehicle.manufacturer,
                    model = event.vehicle.model,
                    color = event.vehicle.color,
                    isVehicleUpdate = true,
                    updateVehicleId = event.vehicle.id ?: ""
                )
                sendUiEvent(UiEvent.Navigate("create"))
            }

            is EditDetailsEvent.DeleteVehicleEvent -> {
                event.vehicle.id?.let { it ->
                    makeApiCall({ api.deleteVehicle(it) }) { message ->
                        sendUiEvent(UiEvent.ShowToast(message.message))
                        _state.value =
                            _state.value.copy(vehicles = _state.value.vehicles.filter { it2 ->
                                it2.id != it
                            })
                    }
                }

            }
        }
    }
}
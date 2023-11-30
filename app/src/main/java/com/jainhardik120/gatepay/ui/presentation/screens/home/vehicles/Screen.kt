package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.Vehicle
import com.jainhardik120.gatepay.ui.BaseViewModel
import com.jainhardik120.gatepay.ui.CollectUiEvents
import com.jainhardik120.gatepay.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun VehiclesScreen() {
    val viewModel = hiltViewModel<VehiclesViewModel>()
    val state = viewModel.state.value
    val navController = rememberNavController()
    CollectUiEvents(navHostController = navController, events = viewModel.uiEvent, hostState = null)
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(onClick = {
                        viewModel.handleEditDetailsEvent(EditDetailsEvent.AddVehicleFabClick)
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text(text = "Add Vehicle")
                    }
                }
            ) {
                LazyColumn(
                    content = {
                        itemsIndexed(state.vehicles) { _, item ->
                            VehicleDetailsCard(
                                vehicle = item,
                                onEditClick = {
                                    viewModel.handleEditDetailsEvent(
                                        EditDetailsEvent.EditVehicleEvent(item)
                                    )
                                }) {
                                viewModel.handleEditDetailsEvent(
                                    EditDetailsEvent.DeleteVehicleEvent(item)
                                )
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                )
            }

        }
        composable("create") {
            Column {
                EditVehicleDetailsScreen(
                    navController = navController,
                    editDetailsViewModel = viewModel
                )
            }
        }
    }

}

@Composable
fun EditVehicleDetailsScreen(
    navController: NavController,
    editDetailsViewModel: VehiclesViewModel
) {
    val editingDetailsState = editDetailsViewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = editingDetailsState.type,
            onValueChange = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.TypeChange(it)
                )
            },
            label = { Text("Type") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editingDetailsState.vehicleNo,
            onValueChange = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.VehicleNoChange(it)
                )
            },
            label = { Text("Vehicle Number") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editingDetailsState.manufacturer,
            onValueChange = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.ManufacturerChange(it)
                )
            },
            label = { Text("Manufacturer") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editingDetailsState.model,
            onValueChange = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.ModelChange(it)
                )
            },
            label = { Text("Model") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editingDetailsState.color,
            onValueChange = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.ColorChange(it)
                )
            },
            label = { Text("Color") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                editDetailsViewModel.handleEditDetailsEvent(
                    EditDetailsEvent.SaveButtonClick
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (editingDetailsState.isVehicleUpdate) {
                    "Save Changes"
                } else {
                    "Create Vehicle"
                }
            )
        }
    }
}


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

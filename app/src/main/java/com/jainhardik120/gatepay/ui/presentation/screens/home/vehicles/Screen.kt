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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.gatepay.ui.CollectUiEvents

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



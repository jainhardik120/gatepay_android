package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.Vehicle
import com.jainhardik120.gatepay.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Composable
fun VehiclesScreen() {
    val viewModel = hiltViewModel<VehiclesViewModel>()
    val vehicles = viewModel.vehicles.collectAsState()
    LazyColumn(content = {
        itemsIndexed(vehicles.value) { index, item ->
            Column {
                Text(text = item.vehicleNo)
            }
        }
    }, modifier = Modifier.fillMaxSize())
}


@HiltViewModel
class VehiclesViewModel @Inject constructor(private val api: GatepayAPI) : BaseViewModel() {
    private var _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> get() = _vehicles.asStateFlow()

    init {
        refreshList()
    }

    private fun refreshList() {
        makeApiCall({ api.listVehicles() }) { arrayVehicles ->
            _vehicles.value = arrayVehicles.map { vehicle ->
                vehicle
            }
        }
    }
}
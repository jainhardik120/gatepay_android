package com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jainhardik120.gatepay.data.remote.dto.Vehicle
import com.jainhardik120.gatepay.ui.theme.GatePayTheme

@Composable
fun VehicleDetailsCard(
    vehicle: Vehicle,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            VehicleDetailItem("Type", vehicle.type)
            VehicleDetailItem("Vehicle Number", vehicle.vehicleNo)
            VehicleDetailItem("Manufacturer", vehicle.manufacturer)
            VehicleDetailItem("Model", vehicle.model)
            VehicleDetailItem("Color", vehicle.color)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }

            }
        }
    }
}

@Composable
fun VehicleDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = "$label : $value"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleDetailsCardPreview() {
    val vehicle = Vehicle(
        type = "Car",
        vehicleNo = "ABC123",
        manufacturer = "Toyota",
        model = "Camry",
        color = "Blue"
    )

    GatePayTheme {
        VehicleDetailsCard(vehicle = vehicle, onEditClick = {}, onDeleteClick = {})
    }
}

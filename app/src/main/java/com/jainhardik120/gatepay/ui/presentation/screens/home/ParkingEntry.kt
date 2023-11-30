package com.jainhardik120.gatepay.ui.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jainhardik120.gatepay.data.remote.dto.ParkingEntry
import com.jainhardik120.gatepay.data.remote.dto.TollGateEntry
import com.jainhardik120.gatepay.ui.presentation.screens.home.transactions.formatTime
import com.jainhardik120.gatepay.ui.theme.GatePayTheme

@Composable
fun CompactParkingEntryCard(parkingEntry: ParkingEntry) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Vehicle: ${parkingEntry.color} ${parkingEntry.manufacturer} ${parkingEntry.model}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Parking Lot: ${parkingEntry.parkinglotname}")
            Text(text = "Entry Time: ${formatTime(parkingEntry.entrytimestamp)}")
            Text(text = "Parking Space Id: ${parkingEntry.parkingspaceid}")
            Text(text = "Vehicle License No: ${parkingEntry.vehicleno}")
        }
    }
}

@Composable
fun CompactTollGateEntryCard(tollGateEntry: TollGateEntry) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Vehicle: ${tollGateEntry.color} ${tollGateEntry.manufacturer} ${tollGateEntry.model}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Toll Gate: ${tollGateEntry.tollgatename}")
            Text(text = "Entry Time: ${formatTime(tollGateEntry.entrytimestamp)}")
            Text(text = "Entry Gate: ${tollGateEntry.locationcoordinates}")
            Text(text = "Vehicle License No: ${tollGateEntry.vehicleno}")
        }
    }
}

@Composable
@Preview
fun CompactTollGateEntryCardPreview() {
    val tollGateEntry = TollGateEntry(
        entryid = "cc7203ef-787c-4aa2-a4d8-e6119842e625",
        vehicleid = "cb19988a-b5c7-4102-8c09-b4376f386699",
        entrytimestamp = "2023-11-30T15:38:17.639Z",
        vehicleno = "KA01AB1567",
        manufacturer = "Maruti Suzuki",
        model = "Swift",
        color = "Red",
        locationcoordinates = "Meerut",
        tollgatename = "Delhi-Meerut Expressway"
    )

    CompactTollGateEntryCard(tollGateEntry = tollGateEntry)
}


@Composable
fun ParkingEntryCardPreview() {
    val parkingEntry = ParkingEntry(
        color = "Blue",
        entryexitid = "12345",
        entrytimestamp = "2023-11-30 12:00 PM",
        manufacturer = "Toyota",
        model = "Camry",
        parkinglotid = "A1",
        parkinglotname = "Main Parking Lot",
        parkingspaceid = "P123",
        vehicleid = "V567",
        vehicleno = "ABC123"
    )

    CompactParkingEntryCard(parkingEntry = parkingEntry)
}

@Preview(showBackground = true)
@Composable
fun ParkingEntryCardPreviewDark() {
    GatePayTheme(darkTheme = true) {
        ParkingEntryCardPreview()
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingEntryCardPreviewLight() {
    GatePayTheme(darkTheme = false) {
        ParkingEntryCardPreview()
    }
}

@Preview(showBackground = true)
@Composable
fun TollEntryCardPreviewDark() {
    GatePayTheme(darkTheme = true) {
        CompactTollGateEntryCardPreview()
    }
}

@Preview(showBackground = true)
@Composable
fun TollEntryCardPreviewLight() {
    GatePayTheme(darkTheme = false) {
        CompactTollGateEntryCardPreview()
    }
}

package com.jainhardik120.gatepay.ui.presentation.screens.home.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.gatepay.data.remote.dto.ParkingEntryX
import com.jainhardik120.gatepay.data.remote.dto.TollGateEntryX
import com.jainhardik120.gatepay.ui.presentation.screens.home.transactions.formatTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen() {
    val viewModel = hiltViewModel<HistoryViewModel>()

    var state by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        2
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            state = page
        }
    }

    val titles = listOf("Parking Lots", "Toll Gates")
    Column {
        SecondaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    onClick = {
                        state = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    selected = (index == state)
                )
            }
        }
        HorizontalPager(state = pagerState, modifier = Modifier
            .weight(1f)
            .fillMaxSize()) { page ->
            when (page) {
                0 -> {
                    LazyColumn {
                        itemsIndexed(viewModel.state.value.parkingEntries) { _, item ->
                            CompactParkingEntryCard(parkingEntryX = item)
                        }
                    }
                }

                1 -> {
                    LazyColumn {
                        itemsIndexed(viewModel.state.value.tollGateEntries) { _, item ->
                            CompactTollGateEntryCard(tollGateEntryX = item)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CompactTollGateEntryCard(tollGateEntryX: TollGateEntryX) {
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
                text = "Vehicle: ${tollGateEntryX.color} ${tollGateEntryX.manufacturer} ${tollGateEntryX.model}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Toll Gate: ${tollGateEntryX.tollgatename}")
            Text(text = "Entry Time: ${formatTime(tollGateEntryX.entrytimestamp)}")
            Text(text = "Entry Gate: ${tollGateEntryX.entryname}")
            Text(text = "Exit Time: ${formatTime(tollGateEntryX.exittimestamp)}")
            Text(text = "Exit Gate: ${tollGateEntryX.exitname}")
            Text(text = "Vehicle License No: ${tollGateEntryX.vehicleno}")
            Text(text = "Amount: ${tollGateEntryX.amount}")
            Text(text = "Transaction ID: ${tollGateEntryX.transactionid}")
        }
    }
}

@Composable
fun CompactParkingEntryCard(parkingEntryX: ParkingEntryX) {
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
                text = "Vehicle: ${parkingEntryX.color} ${parkingEntryX.manufacturer} ${parkingEntryX.model}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Parking Lot: ${parkingEntryX.parkinglotname}")
            Text(text = "Entry Time: ${formatTime(parkingEntryX.entrytimestamp)}")
            Text(text = "Exit Time: ${formatTime(parkingEntryX.exittimestamp)}")
            Text(text = "Parking Space Id: ${parkingEntryX.parkingspaceid}")
            Text(text = "Vehicle License No: ${parkingEntryX.vehicleno}")
            Text(text = "Charges: ${parkingEntryX.charges}")
            Text(text = "Transaction ID: ${parkingEntryX.transactionid}")
        }
    }
}


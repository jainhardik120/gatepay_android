package com.jainhardik120.gatepay.ui.presentation.screens.home.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val api: GatepayAPI) : BaseViewModel() {
    private var _state = mutableStateOf(HistoryState())
    val state: State<HistoryState> = _state

    init {
        refreshList()
    }

    private fun refreshList() {
        makeApiCall({
            api.pastVehicleHistory()
        }) {
            _state.value = _state.value.copy(
                tollGateEntries = it.tollGateEntries,
                parkingEntries = it.parkingEntries
            )
        }
    }
}
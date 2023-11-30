package com.jainhardik120.gatepay.ui.presentation.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: GatepayAPI
) : BaseViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private var _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private fun refresh() {
        makeApiCall({
            api.checkBalance()
        }, onSuccess = {
            _state.value = _state.value.copy(userBalance = it.balance)
        })
        makeApiCall({
            api.currentVehicleEntries()
        }) {
            _state.value = _state.value.copy(
                parkingEntries = it.parkingEntries,
                tollEntries = it.tollGateEntries
            )
        }
    }

    init {
        refresh()
    }
}
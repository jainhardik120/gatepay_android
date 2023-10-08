package com.jainhardik120.gatepay.ui

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()

    data class ShowToast(val message: String) : UiEvent()
    data object NavigateBack : UiEvent()
}
package com.jainhardik120.gatepay.ui.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    data object Transactions : BottomBarScreen(
        route = "transactions",
        title = "Transactions",
        icon = Icons.Filled.ReceiptLong
    )

    data object Vehicles : BottomBarScreen(
        route = "vehicles",
        title = "Vehicles",
        icon = Icons.Filled.Garage
    )

    data object History : BottomBarScreen(
        route = "history",
        title = "History",
        icon = Icons.Filled.History
    )
}
package com.jainhardik120.gatepay.ui.presentation

sealed class AppRoutes(val route: String) {
    object LoginScreen : AppRoutes("login_screen")
    object HomeScreen : AppRoutes("home_screen")
}
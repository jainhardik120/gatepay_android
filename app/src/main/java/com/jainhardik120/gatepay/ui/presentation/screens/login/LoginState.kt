package com.jainhardik120.gatepay.ui.presentation.screens.login

data class LoginState(
    val loginEmail: String = "",
    val loginPassword: String = "",
    val registerEmail: String = "",
    val registerName: String = "",
    val registerPassword: String = "",
    val confirmPassword: String = "",
    val loading: Boolean = false
)
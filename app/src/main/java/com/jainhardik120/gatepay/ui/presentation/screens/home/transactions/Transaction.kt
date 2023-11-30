package com.jainhardik120.gatepay.ui.presentation.screens.home.transactions

data class Transaction(
    val id: String,
    val title: String,
    val date: String,
    val status: String,
    val finalAmount: String,
    val transactionAmount: String
)
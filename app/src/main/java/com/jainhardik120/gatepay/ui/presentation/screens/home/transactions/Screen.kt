package com.jainhardik120.gatepay.ui.presentation.screens.home.transactions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionsScreen() {
    val viewModel = hiltViewModel<TransactionsViewModel>()
    val transactions = viewModel.transactions.collectAsState()
    LazyColumn(content = {
        itemsIndexed(transactions.value) { _, item ->
            TransactionCard(transaction = item)
        }
    }, modifier = Modifier.fillMaxSize())
}

fun formatTime(unformattedTime: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = sdf.parse(unformattedTime)
    val formattedSdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
    return date?.let { formattedSdf.format(it) } ?: unformattedTime
}


fun transactionToTitle(transaction: com.jainhardik120.gatepay.data.remote.dto.Transaction): String {
    return if (transaction.transactionType == "Recharge")
        transaction.transactionType
    else
        "${transaction.transactionType} ${
            transaction.tollOrParkingName?.let {
                "to $it"
            }
        }"
}
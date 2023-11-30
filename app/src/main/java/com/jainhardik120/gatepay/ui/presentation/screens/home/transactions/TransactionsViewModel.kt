package com.jainhardik120.gatepay.ui.presentation.screens.home.transactions

import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val api: GatepayAPI) : BaseViewModel() {
    private var _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> get() = _transactions.asStateFlow()

    init {
        refreshList()
    }

    private fun refreshList() {
        makeApiCall({ api.userTransactions() }) { arrayOfTransactions ->
            _transactions.value = arrayOfTransactions.map { transaction ->
                Transaction(
                    id = transaction.transactionId,
                    title = transactionToTitle(transaction),
                    date = formatTime(transaction.date),
                    status = transaction.status,
                    finalAmount = transaction.endBalance,
                    transactionAmount = transaction.amount
                )
            }
        }
    }
}
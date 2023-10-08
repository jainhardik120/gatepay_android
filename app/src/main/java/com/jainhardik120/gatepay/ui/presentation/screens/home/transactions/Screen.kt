package com.jainhardik120.gatepay.ui.presentation.screens.home.transactions

import android.os.Build
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@Composable
fun TransactionsScreen() {
    val viewModel = hiltViewModel<TransactionsViewModel>()
    val transactions = viewModel.transactions.collectAsState()
    LazyColumn(content = {
        itemsIndexed(transactions.value) { _, item ->
            TransactionItem(transaction = item)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row {
        Column {
            Text(text = transaction.title)
            Text(text = transaction.date)
            Text(text = transaction.status)
            Text(text = transaction.transactionAmount)
        }
        Text(text = transaction.finalAmount)
    }
}

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
                    date = timeAgoText(transaction.date),
                    status = transaction.status,
                    finalAmount = transaction.endBalance,
                    transactionAmount = transaction.amount
                )
            }
        }
    }
}

data class Transaction(
    val id: String,
    val title: String,
    val date: String,
    val status: String,
    val finalAmount: String,
    val transactionAmount: String
)

fun timeAgoText(text: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val date: Date? = sdf.parse(text)
        if (date != null) {
            val timeInMillis: Long = date.time
            val currentTimeInMillis = System.currentTimeMillis()
            return DateUtils.getRelativeTimeSpanString(
                timeInMillis,
                currentTimeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDateTime = LocalDateTime.now()
        val currentZone = ZoneId.systemDefault()
        val dateTime = LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME)
        val gmtZone = ZoneId.of("GMT")

        val difference = Duration.between(
            dateTime.atZone(gmtZone).toInstant(),
            currentDateTime.atZone(currentZone).toInstant()
        )
        val seconds = difference.seconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7

        when {
            weeks > 0 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
        }
    } else {
        text
    }
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
package com.jainhardik120.gatepay.ui.presentation.screens.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.Transaction
import com.jainhardik120.gatepay.ui.BaseViewModel
import com.jainhardik120.gatepay.ui.CollectUiEvents
import com.jainhardik120.gatepay.ui.RechargeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val navController = rememberNavController()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    CollectUiEvents(
        navHostController = navController, events = viewModel.uiEvent, hostState = snackBarHostState
    )
    CheckNotificationPermission()
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {})

    val state = viewModel.state.value
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NavHost(navController, startDestination = "home", modifier = Modifier.padding(it)) {
                composable(route = "home") {
                    Column {
                        Text(text = "Your Balance is INR. ${state.userBalance}")
                        Button(onClick = {
                            val intent = Intent(context, RechargeActivity::class.java)
                            launcher.launch(intent)
                        }) {
                            Text("Recharge")
                        }
                    }
                    LazyColumn(content = {
                        itemsIndexed(state.transactions) { index, item ->
                            Column {
                                Text(text = item.transactionId)
                                Text(text = item.amount)
                            }
                        }
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckNotificationPermission() {
    if (Build.VERSION.SDK_INT >= TIRAMISU) {
        val notificationPermissionState =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        val openAlertDialog = rememberSaveable { mutableStateOf(false) }
        if (!notificationPermissionState.status.isGranted) {
            LaunchedEffect(key1 = Unit, block = {
                openAlertDialog.value = true
            })
        }
        if (openAlertDialog.value) {
            AlertDialog(onDismissRequest = { openAlertDialog.value = false }, confirmButton = {
                TextButton(onClick = {
                    openAlertDialog.value = false
                    notificationPermissionState.launchPermissionRequest()
                }) {
                    Text(text = "Grant Permission")
                }
            }, dismissButton = {
                TextButton(onClick = {
                    openAlertDialog.value = false
                }) {
                    Text(text = "Cancel")
                }
            }, title = {
                Text(text = "Permission Required")
            }, text = {
                Text(text = "Notification permission is required for proper functioning of the app")
            })
        }
    }
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: GatepayAPI
) : BaseViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private var _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        makeApiCall({
            api.checkBalance()
        }, onSuccess = {
            _state.value = _state.value.copy(userBalance = it.balance)
        })

        makeApiCall({ api.userTransactions() }) { transactions ->
            Log.d(TAG, "UserTransactions: ${transactions.size}")
            transactions.forEach {
                Log.d(TAG, "UserTransactions: $it")
            }
            _state.value = _state.value.copy(transactions = transactions.map { it })
        }

        makeApiCall({ api.listVehicles() }) {
            it.forEach {
                Log.d(TAG, "Vehicle: $it")
            }
        }
    }

}

data class HomeState(
    val userBalance: Double = 1000.0, val transactions: List<Transaction> = emptyList()
)
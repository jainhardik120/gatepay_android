package com.jainhardik120.gatepay.ui.presentation.screens.home

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.gatepay.R
import com.jainhardik120.gatepay.ui.CollectUiEvents
import com.jainhardik120.gatepay.ui.RechargeActivity
import com.jainhardik120.gatepay.ui.presentation.screens.home.history.HistoryScreen
import com.jainhardik120.gatepay.ui.presentation.screens.home.transactions.TransactionsScreen
import com.jainhardik120.gatepay.ui.presentation.screens.home.vehicles.VehiclesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
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
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }, bottomBar = {
        val screens = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Vehicles,
            BottomBarScreen.Transactions,
            BottomBarScreen.History
        )
        NavigationBar {
            screens.forEachIndexed { _, screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = {
                        Text(text = screen.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route?.contains(screen.route) ?: false
                    } == true,
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NavHost(navController, startDestination = "home", modifier = Modifier.padding(it)) {
                composable(route = "home") {
                    Box(Modifier.fillMaxSize())
                    Column {
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            item {
                                WalletCard(userBalance = state.userBalance, onClick = {
                                    val intent = Intent(context, RechargeActivity::class.java)
                                    launcher.launch(intent)
                                })
                            }
                            item {
                                if (state.parkingEntries.isNotEmpty()) {
                                    Text(
                                        text = "Parking Space Entries",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            itemsIndexed(state.parkingEntries) { _, item ->
                                CompactParkingEntryCard(parkingEntry = item)
                            }
                            item {
                                if (state.tollEntries.isNotEmpty()) {
                                    Text(
                                        text = "Toll Gate Entries",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            itemsIndexed(state.tollEntries) { _, item ->
                                CompactTollGateEntryCard(tollGateEntry = item)
                            }
                        }
                    }
                }
                composable(route = "transactions") {
                    TransactionsScreen()
                }
                composable(route = "vehicles") {
                    VehiclesScreen()
                }
                composable(route = "history") {
                    HistoryScreen()
                }
            }
        }
    }
}

@Composable
fun WalletCard(userBalance: Double, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Wallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Your Wallet",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Balance: INR $userBalance",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Recharge", fontWeight = FontWeight.Bold)
            }
        }
    }
}

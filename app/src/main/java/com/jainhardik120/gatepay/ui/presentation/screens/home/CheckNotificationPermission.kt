package com.jainhardik120.gatepay.ui.presentation.screens.home

import android.Manifest
import android.os.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
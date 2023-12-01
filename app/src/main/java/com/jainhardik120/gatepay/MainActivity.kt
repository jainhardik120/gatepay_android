package com.jainhardik120.gatepay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.jainhardik120.gatepay.ui.presentation.App
import com.jainhardik120.gatepay.ui.theme.GatePayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
                setContent {
                    GatePayTheme(dynamicColor = false) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            App()
                        }
                    }
                }
            }
        )
    }
}
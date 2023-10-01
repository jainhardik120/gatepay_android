package com.jainhardik120.gatepay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jainhardik120.gatepay.ui.presentation.App
import com.jainhardik120.gatepay.ui.theme.GatePayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GatePayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val viewModel: LoginViewModel = hiltViewModel()
//                    val context = LocalContext.current
//                    val launcher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.StartIntentSenderForResult(),
//                        onResult = viewModel::handleIntentResult
//                    )
//
//                    Column() {
//                        Button(onClick = {
//                            viewModel.launchOneTapIntent(context, launcher)
//                        }) {
//                            Text(text = "Google Sign In")
//                        }
//                    }
                    App()
                }
            }
        }
    }
}
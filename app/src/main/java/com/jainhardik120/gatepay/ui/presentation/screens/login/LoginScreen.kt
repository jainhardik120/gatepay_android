package com.jainhardik120.gatepay.ui.presentation.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.gatepay.ui.CollectUiEvents

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val navController = rememberNavController()

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    CollectUiEvents(
        navHostController = navController,
        events = viewModel.uiEvent,
        hostState = snackBarHostState
    )

    val state = viewModel.state.value

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = viewModel::handleIntentResult
    )

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NavHost(navController, startDestination = "signIn", modifier = Modifier.padding(it)) {
                composable("signIn") {

                    SigninPage(
                        emailState = state.email,
                        passwordState = state.password,
                        onEmailChange = { viewModel.onEvent(LoginEvent.LoginEmailChange(it)) },
                        onPasswordChange = { viewModel.onEvent(LoginEvent.LoginPasswordChange(it)) },
                        onLoginClick = { viewModel.onEvent(LoginEvent.LoginButtonClick) },
                        onRegisterClick = { viewModel.onEvent(LoginEvent.CreateAccountButtonClick) },
                        onGoogleSignInClick = {
                            viewModel.onEvent(
                                LoginEvent.GoogleSignInButtonClick(
                                    context,
                                    launcher
                                )
                            )
                        },
                    )
                }
                composable("signUp") {

                }
            }
        }
    }
}


@Composable
fun SigninPage(
    modifier: Modifier = Modifier,
    emailState: String,
    passwordState: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = emailState,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = passwordState,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onLoginClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = onRegisterClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Register")
            }

            Button(
                onClick = onGoogleSignInClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Google Sign In")
            }
        }
    }
}
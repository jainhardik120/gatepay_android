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
                        emailState = state.loginEmail,
                        passwordState = state.loginPassword,
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
                    RegistrationPage(
                        emailState = state.registerEmail,
                        nameState = state.registerName,
                        passwordState = state.registerPassword,
                        confirmPasswordState = state.confirmPassword,
                        onEmailChange = { viewModel.onEvent(LoginEvent.RegisterEmailChange(it)) },
                        onNameChange = { viewModel.onEvent(LoginEvent.RegisterNameChange(it)) },
                        onPasswordChange = { viewModel.onEvent(LoginEvent.RegisterPasswordChange(it)) },
                        onConfirmPasswordChange = {
                            viewModel.onEvent(
                                LoginEvent.ConfirmPasswordChange(
                                    it
                                )
                            )
                        },
                        onRegisterClick = { viewModel.onEvent(LoginEvent.RegisterButtonClick) },
                        onBackToLoginClick = {
                            viewModel.onEvent(LoginEvent.GoBackToLoginButtonClick)
                        },
                        onGoogleSignUpClick = {
                            viewModel.onEvent(
                                LoginEvent.GoogleSignInButtonClick(
                                    context,
                                    launcher
                                )
                            )
                        }
                    )
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

@Composable
fun RegistrationPage(
    modifier: Modifier = Modifier,
    emailState: String,
    nameState: String,
    passwordState: String,
    confirmPasswordState: String,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    onGoogleSignUpClick: () -> Unit
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
            value = nameState,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordState,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            value = confirmPasswordState,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onRegisterClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Register")
            }

            Button(
                onClick = onBackToLoginClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Back to Login")
            }

            Button(
                onClick = onGoogleSignUpClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Google Sign Up")
            }
        }
    }
}

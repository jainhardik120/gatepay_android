package com.jainhardik120.gatepay.ui.presentation.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.gatepay.R
import com.jainhardik120.gatepay.ui.CollectUiEvents

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        Modifier.imePadding(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(id = R.string.app_name))
            })
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            NavHost(navController, startDestination = "signIn") {
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
            if (state.loading) {
                LoadingDialog()
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
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
    val showPassword = rememberSaveable { mutableStateOf(false) }
    Column(modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = emailState,
                onValueChange = { onEmailChange(it) },
                label = {
                    Text(text = "Email or username")
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = passwordState,
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onValueChange = {
                    onPasswordChange(it)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                trailingIcon = {
                    if (showPassword.value) {
                        IconButton(onClick = { showPassword.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Hide Password"
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Show Password"
                            )
                        }
                    }
                }, visualTransformation = if (showPassword.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLoginClick()
                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    onLoginClick()
                }
            ) {
                Text(text = "Sign In")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    Modifier
                        .weight(1F)
                        .fillMaxWidth()
                )
                Text(text = "OR", modifier = Modifier.padding(horizontal = 4.dp))
                Divider(
                    Modifier
                        .weight(1F)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                onClick = { onGoogleSignInClick() }) {
                Text(text = "Sign In With Google")
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(enabled = true, role = Role.Button) {
                    onRegisterClick()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildAnnotatedString {
                    append("New to ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.app_name))
                    }
                    append("? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Register")
                    }
                }
            )
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
    val showPassword = rememberSaveable { mutableStateOf(false) }
    Column(modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = emailState,
                onValueChange = onEmailChange,
                label = {
                    Text(text = "Email")
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = nameState,
                onValueChange = onNameChange,
                label = {
                    Text(text = "Name")
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = passwordState,
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onValueChange = {
                    onPasswordChange(it)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                trailingIcon = {
                    if (showPassword.value) {
                        IconButton(onClick = { showPassword.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Hide Password"
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Show Password"
                            )
                        }
                    }
                }, visualTransformation = if (showPassword.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = confirmPasswordState,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onRegisterClick
            ) {
                Text("Register")
            }
        }
    }
}

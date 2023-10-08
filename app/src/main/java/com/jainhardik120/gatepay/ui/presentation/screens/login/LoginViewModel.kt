package com.jainhardik120.gatepay.ui.presentation.screens.login

import android.app.Activity
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.jainhardik120.gatepay.Constants
import com.jainhardik120.gatepay.data.KeyValueStorage
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.GoogleLoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginResponse
import com.jainhardik120.gatepay.data.remote.dto.SignupRequest
import com.jainhardik120.gatepay.data.remote.dto.UpdateTokenRequest
import com.jainhardik120.gatepay.ui.BaseViewModel
import com.jainhardik120.gatepay.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: GatepayAPI,
    private val keyValueStorage: KeyValueStorage
) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private lateinit var oneTapClient: SignInClient

    private var _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    override fun apiPreExecuting() {
        _state.value = _state.value.copy(loading = true)
    }

    override fun apiDoneExecuting() {
        _state.value = _state.value.copy(loading = false)
    }

    private fun handleSuccessfulLogin(loginResponse: LoginResponse) {
        keyValueStorage.storeValue(KeyValueStorage.LANDING_DONE_KEY, !loginResponse.isNewUser)
        val firebaseToken = keyValueStorage.getValue(KeyValueStorage.FIREBASE_TOKEN_KEY)
        if (firebaseToken == null) {
            keyValueStorage.saveLoginResponse(loginResponse)
        } else {
            makeApiCall({
                api.updateToken(UpdateTokenRequest(firebaseToken), loginResponse.token)
            }, onDoneExecuting = {
                apiDoneExecuting()
                keyValueStorage.saveLoginResponse(loginResponse)
            }) {

            }
        }
    }


    private fun launchOneTapIntent(
        context: Context,
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) {
        viewModelScope.launch {
            oneTapClient = Identity.getSignInClient(context)
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(Constants.GOOGLE_CLIENT_ID)
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()
            oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent).build()
                    launcher.launch(intentSenderRequest)
                } catch (e: Exception) {
                    sendUiEvent(UiEvent.ShowToast("Error while logging in using Google : ${e.localizedMessage}"))
                }
            }.addOnFailureListener { e ->
                sendUiEvent(UiEvent.ShowToast("Error while logging in using Google : ${e.localizedMessage}"))
            }
        }
    }

    fun handleIntentResult(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            return
        }
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            makeApiCall({
                api.googleLogin(GoogleLoginRequest(idToken))
            }, onSuccess = { handleSuccessfulLogin(it) })
        } else {
            sendUiEvent(UiEvent.ShowToast("Error while logging in using Google"))
        }
    }


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginButtonClick -> {
                if (state.value.loginEmail.isNotEmpty() && state.value.loginPassword.isNotEmpty()) {

                    makeApiCall({
                        api.login(LoginRequest(state.value.loginEmail, state.value.loginPassword))
                    }, onSuccess = { handleSuccessfulLogin(it) })
                } else {
                    sendUiEvent(UiEvent.ShowSnackBar("Email and Password Required"))
                }
            }

            is LoginEvent.CreateAccountButtonClick -> {
                sendUiEvent(UiEvent.Navigate("signUp"))
            }

            is LoginEvent.GoogleSignInButtonClick -> {
                launchOneTapIntent(event.context, event.launcher)
            }

            is LoginEvent.LoginEmailChange -> {
                _state.value = _state.value.copy(loginEmail = event.newEmail)
            }

            is LoginEvent.LoginPasswordChange -> {
                _state.value = _state.value.copy(loginPassword = event.newPassword)
            }

            is LoginEvent.ConfirmPasswordChange -> {
                _state.value = _state.value.copy(confirmPassword = event.newConfirmPassword)
            }

            LoginEvent.RegisterButtonClick -> {
                if (state.value.registerEmail.isNotEmpty() && state.value.registerName.isNotEmpty() && state.value.registerPassword.isNotEmpty() && state.value.confirmPassword.isNotEmpty()) {
                    if (state.value.registerPassword != state.value.confirmPassword) {
                        sendUiEvent(UiEvent.ShowSnackBar("Password and Confirm Password must match"))
                    } else {
                        makeApiCall({
                            api.register(
                                SignupRequest(
                                    state.value.registerEmail,
                                    state.value.registerPassword,
                                    state.value.registerName
                                )
                            )
                        }, onSuccess = { handleSuccessfulLogin(it) })
                    }
                } else {
                    sendUiEvent(UiEvent.ShowSnackBar("All fields are necessary"))
                }
            }

            is LoginEvent.RegisterEmailChange -> {
                _state.value = _state.value.copy(registerEmail = event.newEmail)
            }

            is LoginEvent.RegisterNameChange -> {
                _state.value = _state.value.copy(registerName = event.newName)
            }

            is LoginEvent.RegisterPasswordChange -> {
                _state.value = _state.value.copy(registerPassword = event.newPassword)
            }

            LoginEvent.GoBackToLoginButtonClick -> {
                sendUiEvent(UiEvent.NavigateBack)
            }
        }
    }
}



package com.jainhardik120.gatepay.ui.presentation.screens.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.jainhardik120.gatepay.data.remote.AuthApi
import com.jainhardik120.gatepay.data.remote.dto.LoginRequest
import com.jainhardik120.gatepay.ui.BaseViewModel
import com.jainhardik120.gatepay.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: AuthApi
) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }


    private lateinit var oneTapClient: SignInClient

    private var _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state


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
                        .setServerClientId("990209361841-o8pqrkahe8149ofd0u83qbmk2epatu5t.apps.googleusercontent.com")
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

                }
            }.addOnFailureListener { e ->

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
            Log.d(LoginViewModel.TAG, "handleIntentResult: $idToken")
        } else {

        }
    }


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginButtonClick -> {
                if (state.value.email.isNotEmpty() && state.value.password.isNotEmpty()) {

                    makeApiCall({
                        api.login(LoginRequest(state.value.email, state.value.password))
                    }, onSuccess = {

                    })
                } else {
                    sendUiEvent(UiEvent.ShowSnackbar("Email and Password Required"))
                }
            }

            is LoginEvent.CreateAccountButtonClick -> {
                sendUiEvent(UiEvent.Navigate("signUp"))
            }

            is LoginEvent.GoogleSignInButtonClick -> {
                launchOneTapIntent(event.context, event.launcher)
            }

            is LoginEvent.LoginEmailChange -> {
                _state.value = _state.value.copy(email = event.newEmail)
            }

            is LoginEvent.LoginPasswordChange -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
        }
    }
}


data class LoginState(
    val email: String = "",
    val password: String = ""
)

sealed class LoginEvent {
    object LoginButtonClick : LoginEvent()
    object CreateAccountButtonClick : LoginEvent()
    data class GoogleSignInButtonClick(
        val context: Context,
        val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) : LoginEvent()

    data class LoginEmailChange(val newEmail: String) : LoginEvent()
    data class LoginPasswordChange(val newPassword: String) : LoginEvent()
}

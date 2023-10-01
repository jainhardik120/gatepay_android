package com.jainhardik120.gatepay.ui.presentation.screens.login

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest

sealed class LoginEvent {
    object LoginButtonClick : LoginEvent()
    object CreateAccountButtonClick : LoginEvent()
    object GoBackToLoginButtonClick : LoginEvent()
    data class GoogleSignInButtonClick(
        val context: Context,
        val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) : LoginEvent()

    data class LoginEmailChange(val newEmail: String) : LoginEvent()
    data class LoginPasswordChange(val newPassword: String) : LoginEvent()

    object RegisterButtonClick : LoginEvent()
    data class RegisterNameChange(val newName: String) : LoginEvent()
    data class RegisterEmailChange(val newEmail: String) : LoginEvent()
    data class RegisterPasswordChange(val newPassword: String) : LoginEvent()
    data class ConfirmPasswordChange(val newConfirmPassword: String) : LoginEvent()
}
package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.data.remote.dto.GoogleLoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginResponse
import com.jainhardik120.gatepay.data.remote.dto.MessageError
import com.jainhardik120.gatepay.data.remote.dto.SignupRequest

interface AuthApi {
    suspend fun login(request: LoginRequest) : Result<LoginResponse, MessageError>
    suspend fun register(request: SignupRequest) : Result<LoginResponse, MessageError>
    suspend fun googleLogin(request: GoogleLoginRequest) : Result<LoginResponse, MessageError>
}



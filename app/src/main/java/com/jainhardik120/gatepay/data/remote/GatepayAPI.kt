package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.Result
import com.jainhardik120.gatepay.data.remote.dto.AmountRequest
import com.jainhardik120.gatepay.data.remote.dto.BalanceResponse
import com.jainhardik120.gatepay.data.remote.dto.CheckoutResponse
import com.jainhardik120.gatepay.data.remote.dto.CurrentVehicleEntries
import com.jainhardik120.gatepay.data.remote.dto.GoogleLoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginResponse
import com.jainhardik120.gatepay.data.remote.dto.MessageError
import com.jainhardik120.gatepay.data.remote.dto.MessageResponse
import com.jainhardik120.gatepay.data.remote.dto.PastVehicleEntries
import com.jainhardik120.gatepay.data.remote.dto.RazorpayInfo
import com.jainhardik120.gatepay.data.remote.dto.SignupRequest
import com.jainhardik120.gatepay.data.remote.dto.Transaction
import com.jainhardik120.gatepay.data.remote.dto.UpdateTokenRequest
import com.jainhardik120.gatepay.data.remote.dto.Vehicle

interface GatepayAPI {
    suspend fun login(request: LoginRequest): Result<LoginResponse, MessageError>
    suspend fun register(request: SignupRequest): Result<LoginResponse, MessageError>
    suspend fun googleLogin(request: GoogleLoginRequest): Result<LoginResponse, MessageError>
    suspend fun updateToken(
        request: UpdateTokenRequest,
        authToken: String
    ): Result<MessageResponse, MessageError>

    suspend fun checkBalance(): Result<BalanceResponse, MessageError>
    suspend fun checkout(request: AmountRequest): Result<CheckoutResponse, MessageError>
    suspend fun verifyPayment(request: RazorpayInfo): Result<BalanceResponse, MessageError>
    suspend fun userTransactions(): Result<Array<Transaction>, MessageError>

    suspend fun addVehicle(vehicle: Vehicle): Result<Vehicle, MessageError>
    suspend fun deleteVehicle(vehicleId: String): Result<MessageResponse, MessageError>
    suspend fun listVehicles(): Result<List<Vehicle>, MessageError>
    suspend fun editVehicle(vehicle: Vehicle, vehicleId: String): Result<Vehicle, MessageError>

    suspend fun currentVehicleEntries(): Result<CurrentVehicleEntries, MessageError>
    suspend fun pastVehicleHistory(): Result<PastVehicleEntries, MessageError>
}
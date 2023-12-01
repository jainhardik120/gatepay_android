package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.Result
import com.jainhardik120.gatepay.data.KeyValueStorage
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class GatepayAPIImpl(
    private val client: HttpClient,
    private val keyValueStorage: KeyValueStorage
) : GatepayAPI {

    private suspend inline fun <T, reified R> performApiRequest(
        call: () -> T
    ): Result<T, R> {
        return try {
            val response = call.invoke()
            Result.Success(response)
        } catch (e: ClientRequestException) {
            Result.ClientException(e.response.body<R>(), e.response.status)
        } catch (e: Exception) {
            Result.Exception(e.message)
        }
    }

    private fun HttpRequestBuilder.tokenAuthHeaders(headers: HeadersBuilder.() -> Unit = {}) {
        headers {
            keyValueStorage.getToken()?.let {
                bearerAuth(token = it)
            }
            headers()
        }
    }

    private suspend inline fun <reified T, reified R> requestBuilder(
        url: String,
        method: HttpMethod,
        body: T
    ): R {
        return client.request(url) {
            this.method = method
            contentType(ContentType.Application.Json)
            setBody(body)
            tokenAuthHeaders()
        }.body()
    }

    private suspend inline fun <reified T> requestBuilder(
        url: String,
        method: HttpMethod
    ): T {
        return client.request(url) {
            this.method = method
            tokenAuthHeaders()
        }.body()
    }

    override suspend fun login(request: LoginRequest): Result<LoginResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.LOGIN_ROUTE, HttpMethod.Post, request)
        }
    }

    override suspend fun register(request: SignupRequest): Result<LoginResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.SIGNUP_ROUTE, HttpMethod.Post, request)
        }
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): Result<LoginResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.GOOGLE_LOGIN_ROUTE, HttpMethod.Post, request)
        }
    }

    override suspend fun updateToken(
        request: UpdateTokenRequest,
        authToken: String
    ): Result<MessageResponse, MessageError> {
        return performApiRequest {
            client.request(APIRoutes.UPDATE_FIREBASE_TOKEN_ROUTE) {
                this.method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    bearerAuth(authToken)
                }
            }.body()
        }
    }

    override suspend fun checkBalance(): Result<BalanceResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.CHECK_BALANCE_ROUTE, HttpMethod.Get)
        }
    }

    override suspend fun checkout(request: AmountRequest): Result<CheckoutResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.CHECKOUT_ROUTE, HttpMethod.Post, request)
        }
    }

    override suspend fun verifyPayment(request: RazorpayInfo): Result<BalanceResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.VERIFY_PAYMENT_ROUTE, HttpMethod.Post, request)
        }
    }

    override suspend fun userTransactions(): Result<Array<Transaction>, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.USER_TRANSACTIONS_ROUTE, HttpMethod.Get)
        }
    }

    override suspend fun addVehicle(vehicle: Vehicle): Result<Vehicle, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.ADD_VEHICLE_ROUTE, HttpMethod.Post, vehicle)
        }
    }

    override suspend fun deleteVehicle(vehicleId: String): Result<MessageResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.putDeleteVehicleBaseRoute(vehicleId), HttpMethod.Delete)
        }
    }

    override suspend fun listVehicles(): Result<List<Vehicle>, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.LIST_VEHICLES_ROUTE, HttpMethod.Get)
        }
    }

    override suspend fun editVehicle(
        vehicle: Vehicle,
        vehicleId: String
    ): Result<Vehicle, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.putDeleteVehicleBaseRoute(vehicleId), HttpMethod.Put, vehicle)
        }
    }

    override suspend fun currentVehicleEntries(): Result<CurrentVehicleEntries, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.CURRENT_VEHICLE_ENTRIES, HttpMethod.Get)
        }
    }

    override suspend fun pastVehicleHistory(): Result<PastVehicleEntries, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.PAST_VEHICLE_HISTORY, HttpMethod.Get)
        }
    }
}
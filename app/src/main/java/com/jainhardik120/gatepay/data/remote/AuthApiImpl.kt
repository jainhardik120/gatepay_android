package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.data.remote.dto.GoogleLoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginRequest
import com.jainhardik120.gatepay.data.remote.dto.LoginResponse
import com.jainhardik120.gatepay.data.remote.dto.MessageError
import com.jainhardik120.gatepay.data.remote.dto.SignupRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class AuthApiImpl(
    private val client : HttpClient
) : AuthApi {


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
}
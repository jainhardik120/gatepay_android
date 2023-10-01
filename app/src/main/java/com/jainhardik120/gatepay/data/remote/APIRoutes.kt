package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.R
import com.jainhardik120.gatepay.getResourceString

object APIRoutes {
    private val HOST = getResourceString(R.string.server_host_address)
    private val BASE_URL = "https://$HOST/api/auth"

    val LOGIN_ROUTE = "$BASE_URL/login"
    val SIGNUP_ROUTE = "$BASE_URL/register"
    val GOOGLE_LOGIN_ROUTE = "$BASE_URL/googleLogin"
}
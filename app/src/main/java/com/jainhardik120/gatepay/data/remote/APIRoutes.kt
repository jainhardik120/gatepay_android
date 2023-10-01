package com.jainhardik120.gatepay.data.remote

object APIRoutes {
    private const val HOST = "gatepay-server.vercel.app"
    private const val BASE_URL = "https://$HOST/api/auth"

    const val LOGIN_ROUTE = "$BASE_URL/login"
    const val SIGNUP_ROUTE = "$BASE_URL/register"
    const val GOOGLE_LOGIN_ROUTE = "$BASE_URL/googleLogin"
}
package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.Constants

object APIRoutes {
    private const val HOST = Constants.SERVER_BASE_ADDRESS
    private const val AUTH_BASE_URL = "https://$HOST/api/auth"

    const val LOGIN_ROUTE = "$AUTH_BASE_URL/login"
    const val SIGNUP_ROUTE = "$AUTH_BASE_URL/register"
    const val GOOGLE_LOGIN_ROUTE = "$AUTH_BASE_URL/googleLogin"

    private const val PAYMENTS_BASE_URL = "https://$HOST/api/payments"
    const val CHECK_BALANCE_ROUTE = "$PAYMENTS_BASE_URL/checkBalance"
    const val CHECKOUT_ROUTE = "$PAYMENTS_BASE_URL/checkout"
    const val VERIFY_PAYMENT_ROUTE = "$PAYMENTS_BASE_URL/verifyPayment"
}
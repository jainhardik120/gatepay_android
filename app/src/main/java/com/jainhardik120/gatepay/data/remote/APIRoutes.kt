package com.jainhardik120.gatepay.data.remote

import com.jainhardik120.gatepay.Constants

object APIRoutes {
    private const val HOST = Constants.SERVER_BASE_ADDRESS

    private const val AUTH_BASE_URL = "https://$HOST/api/auth"
    const val LOGIN_ROUTE = "$AUTH_BASE_URL/login"
    const val SIGNUP_ROUTE = "$AUTH_BASE_URL/register"
    const val GOOGLE_LOGIN_ROUTE = "$AUTH_BASE_URL/googleLogin"

    const val UPDATE_FIREBASE_TOKEN_ROUTE = "$AUTH_BASE_URL/updateToken"

    private const val PAYMENTS_BASE_URL = "https://$HOST/api/payments"
    const val CHECK_BALANCE_ROUTE = "$PAYMENTS_BASE_URL/checkBalance"
    const val CHECKOUT_ROUTE = "$PAYMENTS_BASE_URL/checkout"
    const val VERIFY_PAYMENT_ROUTE = "$PAYMENTS_BASE_URL/verifyPayment"
    const val USER_TRANSACTIONS_ROUTE = "$PAYMENTS_BASE_URL/userTransactions"

    private const val VEHICLES_BASE_URL = "https://$HOST/api/vehicle"
    const val ADD_VEHICLE_ROUTE = "$VEHICLES_BASE_URL/add"
    const val LIST_VEHICLES_ROUTE = "$VEHICLES_BASE_URL/"
    fun putDeleteVehicleBaseRoute(vehicleId: String): String {
        return "$VEHICLES_BASE_URL/$vehicleId"
    }

    private const val PARKING_BASE_URL = "https://$HOST/api/parking"

    const val CURRENT_VEHICLE_ENTRIES = "$PARKING_BASE_URL/currentVehicleEntries"
    const val PAST_VEHICLE_HISTORY = "$PARKING_BASE_URL/pastVehicleHistory"
}
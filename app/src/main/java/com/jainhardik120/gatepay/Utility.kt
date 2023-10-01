package com.jainhardik120.gatepay

import android.content.res.Resources

fun getResourceString(id: Int): String {
    return Resources.getSystem().getString(id)
}
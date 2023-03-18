package com.pjtm23.explorer.utils.extensions

import android.content.SharedPreferences

fun SharedPreferences.putDouble(key: String, value: Double) =
    edit().putLong(key, value.toLongBits()).apply()

fun SharedPreferences.getDouble(key: String, defaultValue: Double = 0.0) =
    getLong(key, 0L).takeUnless { it == 0L }?.bitsToDouble() ?: defaultValue


private fun Long.bitsToDouble() = java.lang.Double.longBitsToDouble(this)
private fun Double.toLongBits() = java.lang.Double.doubleToRawLongBits(this)

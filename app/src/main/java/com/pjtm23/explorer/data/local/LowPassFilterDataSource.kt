package com.pjtm23.explorer.data.local

import android.content.SharedPreferences
import javax.inject.Inject

private const val DEFAULT_ACCELEROMETER_ALPHA = .1F
private const val DEFAULT_MAGNETOMETER_ALPHA = .3F

private const val LPF_ACCELEROMETER_ALPHA = "lpf_accelerometer_alpha"
private const val LPF_MAGNETOMETER_ALPHA = "lpf_magnetometer_alpha"

// TODO For testing only. To be removed
class LowPassFilterDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getAccelerometerAlpha() =
        sharedPreferences.getFloat(LPF_ACCELEROMETER_ALPHA, DEFAULT_ACCELEROMETER_ALPHA)

    fun getMagnetometerAlpha() =
        sharedPreferences.getFloat(LPF_MAGNETOMETER_ALPHA, DEFAULT_MAGNETOMETER_ALPHA)

    fun setAccelerometerAlpha(alpha: Float) {
        sharedPreferences.edit().putFloat(LPF_ACCELEROMETER_ALPHA, alpha).apply()
    }

    fun setMagnetometerAlpha(alpha: Float) {
        sharedPreferences.edit().putFloat(LPF_MAGNETOMETER_ALPHA, alpha).apply()
    }
}
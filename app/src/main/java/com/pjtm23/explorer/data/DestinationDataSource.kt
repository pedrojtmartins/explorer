package com.pjtm23.explorer.data

import android.content.SharedPreferences
import android.location.Location
import javax.inject.Inject

private const val KEY_LATITUDE = "lat"
private const val KEY_LONGITUDE = "lng"

class DestinationDataSource @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    fun saveDestination(destination: Location) {
        sharedPreferences.edit()
                .putLong(KEY_LATITUDE, destination.latitude.toLongBits())
                .putLong(KEY_LONGITUDE, destination.longitude.toLongBits())
                .apply()
    }

    fun getDestination() = Location(null).apply {
        latitude = sharedPreferences.getLong(KEY_LATITUDE, 0).bitsToDouble()
        longitude = sharedPreferences.getLong(KEY_LONGITUDE, 0).bitsToDouble()
    }

    private fun Long.bitsToDouble() = java.lang.Double.longBitsToDouble(this)
    private fun Double.toLongBits() = java.lang.Double.doubleToRawLongBits(this)
}
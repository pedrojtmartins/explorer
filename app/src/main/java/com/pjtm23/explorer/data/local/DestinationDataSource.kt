package com.pjtm23.explorer.data.local

import android.content.SharedPreferences
import android.location.Location
import com.pjtm23.explorer.utils.extensions.getDouble
import com.pjtm23.explorer.utils.extensions.putDouble
import javax.inject.Inject

private const val KEY_LATITUDE = "lat"
private const val KEY_LONGITUDE = "lng"

class DestinationDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveDestination(destination: Location) {
        with(sharedPreferences) {
            putDouble(KEY_LATITUDE, destination.latitude)
            putDouble(KEY_LONGITUDE, destination.longitude)
        }
    }

    fun getDestination() = Location(null).apply {
        latitude = sharedPreferences.getDouble(KEY_LATITUDE, 0.0)
        longitude = sharedPreferences.getDouble(KEY_LONGITUDE)
    }
}
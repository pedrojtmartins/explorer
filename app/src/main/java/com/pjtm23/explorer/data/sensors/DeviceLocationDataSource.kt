package com.pjtm23.explorer.data.sensors

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val LOCATION_UPDATE_INTERVAL_MS = 1000L
private const val LOCATION_PRIORITY = Priority.PRIORITY_HIGH_ACCURACY

class DeviceLocationDataSource @Inject constructor(
        private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun getLocationUpdates() = callbackFlow {
        val locationRequest = LocationRequest.Builder(LOCATION_UPDATE_INTERVAL_MS)
                .setPriority(LOCATION_PRIORITY)
                .build()

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { trySend(it) }
            }
        }

        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }
}
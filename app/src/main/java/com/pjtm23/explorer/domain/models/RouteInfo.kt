package com.pjtm23.explorer.domain.models

import android.location.Location
import kotlin.math.roundToInt

private const val DISTANCE_TO_COMPLETE_ROUTE = 15 // meters

data class RouteInfo(
        val currentLocation: Location,
        val destination: Location
) {

    val distance: Int // meters
        get() = currentLocation.distanceTo(destination).roundToInt()

    val bearing: Int // degrees
        get() = currentLocation.bearingTo(destination).roundToInt()

    val isComplete
        get() = distance <= DISTANCE_TO_COMPLETE_ROUTE

    val isValid: Boolean
        get() {
            val invalid = 0.toDouble()
            return (currentLocation.latitude != invalid || currentLocation.longitude != invalid) &&
                    (destination.latitude != invalid || destination.latitude != invalid)
        }
}
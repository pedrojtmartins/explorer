package com.pjtm23.explorer.domain.models

import android.location.Location
import kotlin.math.roundToInt

private const val DISTANCE_TO_COMPLETE_ROUTE = 15 // meters

data class RouteInfo(
        val currentLocation: Location,
        val destination: Location,
        val deviceOrientation: DeviceOrientation
) {

    val distance: Int // meters
        get() = currentLocation.distanceTo(destination).roundToInt()

    val currentBearing: Int
        get() = currentLocation.bearing.roundToInt()

    val targetBearing: Int // degrees
        get() = currentLocation.bearingTo(destination).roundToInt()

    val isComplete: Boolean
        get() = distance <= DISTANCE_TO_COMPLETE_ROUTE

    val isValid: Boolean
        get() = (currentLocation.latitude != 0.0 || currentLocation.longitude != 0.0) &&
                (destination.latitude != 0.0 || destination.latitude != 0.0)
}
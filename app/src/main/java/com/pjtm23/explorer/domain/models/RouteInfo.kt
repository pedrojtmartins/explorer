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

    val targetBearing: Int // degrees
        get() = currentLocation.bearingTo(destination).roundToInt()

    val deviceBearing: Int // degrees
        get() = deviceOrientation.azimuth.roundToInt()

    val bearingOffset: Int // degrees
        get() = targetBearing - deviceBearing

    val isComplete: Boolean
        get() = distance <= DISTANCE_TO_COMPLETE_ROUTE

    val isValid: Boolean
        get() = currentLocation.isValid() && destination.isValid() && deviceOrientation.isValid()
}

fun Location.isValid() = latitude != 0.0 || longitude != 0.0
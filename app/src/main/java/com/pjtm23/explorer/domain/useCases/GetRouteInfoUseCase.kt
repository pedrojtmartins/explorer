package com.pjtm23.explorer.domain.useCases

import android.location.Location
import com.pjtm23.explorer.data.local.DestinationDataSource
import com.pjtm23.explorer.data.sensors.DeviceLocationDataSource
import com.pjtm23.explorer.data.sensors.DeviceOrientationDataSource
import com.pjtm23.explorer.domain.models.DeviceOrientation
import com.pjtm23.explorer.domain.models.RouteInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetRouteInfoUseCase @Inject constructor(
        private val deviceLocationDataSource: DeviceLocationDataSource,
        private val destinationDataSource: DestinationDataSource,
        private val deviceOrientationDataSource: DeviceOrientationDataSource
) {

    operator fun invoke(): Flow<RouteInfo> {
        val destination = destinationDataSource.getDestination()
        return deviceLocationDataSource.getLocationUpdates().combine(
            deviceOrientationDataSource()
        ) { currentLocation: Location, deviceOrientation: DeviceOrientation ->
            RouteInfo(
                currentLocation = currentLocation,
                destination = destination,
                deviceOrientation = deviceOrientation
            )

        }
    }
}
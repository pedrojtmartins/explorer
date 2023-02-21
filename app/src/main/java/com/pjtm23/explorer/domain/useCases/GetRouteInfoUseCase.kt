package com.pjtm23.explorer.domain.useCases

import com.pjtm23.explorer.data.DestinationDataSource
import com.pjtm23.explorer.data.DeviceLocationDataSource
import com.pjtm23.explorer.domain.models.RouteInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRouteInfoUseCase @Inject constructor(
        private val deviceLocationDataSource: DeviceLocationDataSource,
        private val destinationDataSource: DestinationDataSource
) {

    operator fun invoke(): Flow<RouteInfo> {
        val destination = destinationDataSource.getDestination()
        return deviceLocationDataSource.getLocationUpdates().map { currentLocation ->
            RouteInfo(
                    currentLocation = currentLocation,
                    destination = destination
            )
        }
    }
}
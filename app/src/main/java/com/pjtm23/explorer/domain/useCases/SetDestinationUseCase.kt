package com.pjtm23.explorer.domain.useCases

import android.location.Location
import com.pjtm23.explorer.data.DestinationDataSource
import javax.inject.Inject

class SetDestinationUseCase @Inject constructor(
        private val destinationDataSource: DestinationDataSource
) {

    operator fun invoke(latitude: Double, longitude: Double): Boolean {
        val location = Location(null).apply {
            setLatitude(latitude)
            setLongitude(longitude)
        }

        if (!location.isValid()) {
            return false
        }

        destinationDataSource.saveDestination(location)
        return true
    }

    private fun Location.isValid() = latitude != 0.0 || longitude != 0.0
}
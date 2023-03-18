package com.pjtm23.explorer.domain.useCases

import android.location.Location
import com.pjtm23.explorer.data.local.DestinationDataSource
import javax.inject.Inject

class GetDestinationUseCase @Inject constructor(
        private val destinationDataSource: DestinationDataSource
) {

    operator fun invoke(): Location = destinationDataSource.getDestination()
}
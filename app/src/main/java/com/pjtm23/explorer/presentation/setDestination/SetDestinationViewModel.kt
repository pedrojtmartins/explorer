package com.pjtm23.explorer.presentation.setDestination

import com.mapbox.geojson.Point
import com.pjtm23.explorer.data.local.LowPassFilterDataSource
import com.pjtm23.explorer.domain.models.isValid
import com.pjtm23.explorer.domain.useCases.GetDestinationUseCase
import com.pjtm23.explorer.domain.useCases.SetDestinationUseCase
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent.DestinationSet
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Accelerometer
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.AddMarker
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Confirm
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Magnetometer
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.RemoveMarker
import com.pjtm23.explorer.utils.navigation.NavigationEvent
import com.pjtm23.explorer.utils.navigation.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SetDestinationViewModel @Inject constructor(
    private val getDestination: GetDestinationUseCase,
    private val setDestination: SetDestinationUseCase,
    private val lowPassFilterDataSource: LowPassFilterDataSource
) : NavigationViewModel() {

    private val _viewState = MutableStateFlow(SetDestinationViewState())
    val viewState = _viewState.asStateFlow()

    init {
        initViewState()
    }

    private fun initViewState() {
        _viewState.update {
            it.copy(
                marker = getDestination().takeIf { it.isValid() }?.let { prevDestination ->
                    Point.fromLngLat(
                        prevDestination.longitude,
                        prevDestination.latitude
                    )
                },
                accAlpha = lowPassFilterDataSource.getAccelerometerAlpha().toString(),
                magAlpha = lowPassFilterDataSource.getMagnetometerAlpha().toString()
            )
        }
    }

    fun onViewEvent(event: SetDestinationViewEvent) {
        when (event) {
            is Confirm -> onConfirm()
            is AddMarker -> addMarker(event.point)
            is RemoveMarker -> removeMarker()
            is Accelerometer -> setAccelerometer(event.value)
            is Magnetometer -> setMagnetometer(event.value)
        }
    }

    // TODO For testing only. To be removed
    private fun setAccelerometer(value: String) {
        _viewState.update {
            it.copy(accAlpha = value)
        }

        value.toFloatOrNull()?.let {
            lowPassFilterDataSource.setAccelerometerAlpha(it)
        }
    }

    // TODO For testing only. To be removed
    private fun setMagnetometer(value: String) {
        _viewState.update {
            it.copy(magAlpha = value)
        }

        value.toFloatOrNull()?.let { alpha ->
            lowPassFilterDataSource.setMagnetometerAlpha(alpha)
        }
    }

    private fun addMarker(point: Point) {
        _viewState.update {
            it.copy(marker = point)
        }
    }

    private fun removeMarker() {
        _viewState.update { it.copy(marker = null) }
    }

    private fun onConfirm() {
        _viewState.value.marker?.let {
            setDestination(it.latitude(), it.longitude())
            navigateTo(DestinationSet)
        }
    }
}

data class SetDestinationViewState(
    val marker: Point? = null,

    // TODO For testing only. To be removed
    val accAlpha: String = "",
    val magAlpha: String = ""
)

sealed interface SetDestinationViewEvent {
    data class AddMarker(val point: Point) : SetDestinationViewEvent
    data class RemoveMarker(val point: Point) : SetDestinationViewEvent
    object Confirm : SetDestinationViewEvent

    // TODO For testing only. To be removed
    data class Accelerometer(val value: String) : SetDestinationViewEvent
    data class Magnetometer(val value: String) : SetDestinationViewEvent
}

sealed interface SetDestinationNavigationEvent : NavigationEvent {
    object DestinationSet : SetDestinationNavigationEvent
}
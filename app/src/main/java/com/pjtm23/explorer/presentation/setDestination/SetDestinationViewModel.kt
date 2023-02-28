package com.pjtm23.explorer.presentation.setDestination

import com.pjtm23.explorer.domain.useCases.SetDestinationUseCase
import com.pjtm23.explorer.navigation.NavigationEvent
import com.pjtm23.explorer.navigation.NavigationViewModel
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent.DestinationSet
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewError.InvalidDestination
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Confirm
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.LatitudeUpdated
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.LongitudeUpdated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SetDestinationViewModel @Inject constructor(
        val setDestination: SetDestinationUseCase
) : NavigationViewModel() {

    private val _viewState = MutableStateFlow(SetDestinationViewState())
    val viewState = _viewState.asStateFlow()

    fun onViewEvent(event: SetDestinationViewEvent) {
        _viewState.update { it.copy(error = null) }

        when (event) {
            is LatitudeUpdated -> _viewState.update { it.copy(latitude = event.latitude) }
            is LongitudeUpdated -> _viewState.update { it.copy(longitude = event.longitude) }
            is Confirm -> onConfirm()
        }
    }

    private fun onConfirm() {
        val isSet = with(_viewState.value) {
            val dLatitude = latitude.toDoubleOrNull()
            val dLongitude = longitude.toDoubleOrNull()

            if (dLatitude == null || dLongitude == null) {
                return@with false
            }

            setDestination(dLatitude, dLongitude)
        }

        if (isSet) {
            navigateTo(DestinationSet)
        } else {
            _viewState.update { it.copy(error = InvalidDestination) }
        }
    }
}

data class SetDestinationViewState(
        val latitude: String = "",
        val longitude: String = "",
        val error: SetDestinationViewError? = null
)

sealed interface SetDestinationViewError {
    object InvalidDestination : SetDestinationViewError
}

sealed interface SetDestinationViewEvent {
    data class LatitudeUpdated(val latitude: String) : SetDestinationViewEvent
    data class LongitudeUpdated(val longitude: String) : SetDestinationViewEvent
    object Confirm : SetDestinationViewEvent
}

sealed interface SetDestinationNavigationEvent : NavigationEvent {
    object DestinationSet : SetDestinationNavigationEvent
}
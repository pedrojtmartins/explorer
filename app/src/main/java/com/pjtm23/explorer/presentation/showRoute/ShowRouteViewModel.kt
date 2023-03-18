package com.pjtm23.explorer.presentation.showRoute

import androidx.lifecycle.viewModelScope
import com.pjtm23.explorer.domain.models.RouteInfo
import com.pjtm23.explorer.domain.useCases.GetRouteInfoUseCase
import com.pjtm23.explorer.utils.navigation.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowRouteViewModel @Inject constructor(
        private val getRouteInfo: GetRouteInfoUseCase,
) : NavigationViewModel() {

    private val _viewState = MutableStateFlow(ShowRouteViewState())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getRouteInfo().collect(::updateRouteState)
        }
    }

    private fun updateRouteState(routeInfo: RouteInfo) {
        _viewState.update {
            it.copy(
                    distance = routeInfo.distance,
                    deviceBearing = routeInfo.deviceBearing,
                    targetBearing = routeInfo.targetBearing,
                    bearingOffset = routeInfo.bearingOffset
            )
        }
    }
}

data class ShowRouteViewState(
        val distance: Int = 0,
        val deviceBearing: Int = 0,
        val targetBearing: Int = 0,
        val bearingOffset: Int = 0
)
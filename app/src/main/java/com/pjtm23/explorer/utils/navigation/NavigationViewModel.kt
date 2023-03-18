package com.pjtm23.explorer.utils.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class NavigationViewModel : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvent.asSharedFlow()

    protected fun navigateTo(event: NavigationEvent) {
        viewModelScope.launch { _navigationEvent.emit(event) }
    }
}
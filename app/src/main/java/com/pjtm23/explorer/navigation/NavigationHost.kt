package com.pjtm23.explorer.navigation

interface NavigationHost {

    val coordinator: NavigationCoordinator

    fun onNavigationEvent(event: NavigationEvent) = coordinator.onNavigationEvent(event)
}

package com.pjtm23.explorer.presentation.showRoute

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val showRouteNavigationRoute = "showRoute"

fun NavController.navigateToShowRoute(navOptions: NavOptions? = null) {
    navigate(
            showRouteNavigationRoute,
            navOptions
    )
}

fun NavGraphBuilder.showRouteScreen() {
    composable(route = showRouteNavigationRoute) {
        ShowRouteScreen()
    }
}
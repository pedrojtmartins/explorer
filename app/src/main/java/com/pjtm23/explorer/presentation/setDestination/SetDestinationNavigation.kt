package com.pjtm23.explorer.presentation.setDestination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val setDestinationNavigationRoute = "setDestination"

fun NavGraphBuilder.setDestinationScreen(onDestinationSet: () -> Unit) {
    composable(route = setDestinationNavigationRoute) {
        SetDestinationScreen(onDestinationSet = onDestinationSet)
    }
}
package com.pjtm23.explorer.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pjtm23.explorer.navigation.NavigationCoordinator
import com.pjtm23.explorer.navigation.NavigationEvent
import com.pjtm23.explorer.presentation.ExplorerRoute.SetDestination
import com.pjtm23.explorer.presentation.ExplorerRoute.ShowRoute
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent.DestinationSet
import com.pjtm23.explorer.presentation.setDestination.SetDestinationScreen
import com.pjtm23.explorer.presentation.showRoute.ShowRouteScreen
import javax.inject.Inject

sealed class ExplorerRoute(val route: String) {
    object SetDestination : ExplorerRoute("setDestination")
    object ShowRoute : ExplorerRoute("showRoute")
}

class ExplorerCoordinator @Inject constructor() : NavigationCoordinator() {

    @Composable
    fun NavHost(
            activity: ComponentActivity,
            modifier: Modifier = Modifier,
            navController: NavHostController = rememberNavController(),
            startDestination: String = SetDestination.route
    ) {
        bind(activity, navController)
        NavHost(
                modifier = modifier,
                navController = navController,
                startDestination = startDestination
        ) {
            composable(SetDestination.route) { SetDestinationScreen() }
            composable(ShowRoute.route) { ShowRouteScreen() }
        }
    }

    override fun onNavigationEvent(event: NavigationEvent): Boolean {
        when (event) {
            is SetDestinationNavigationEvent -> handleSetDestinationNavigationEvent(event)
            else -> return false
        }

        return true
    }

    private fun handleSetDestinationNavigationEvent(event: SetDestinationNavigationEvent) {
        when (event) {
            DestinationSet -> navController.navigate(ShowRoute.route)
        }
    }
}
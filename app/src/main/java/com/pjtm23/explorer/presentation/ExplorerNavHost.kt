package com.pjtm23.explorer.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pjtm23.explorer.presentation.setDestination.setDestinationNavigationRoute
import com.pjtm23.explorer.presentation.setDestination.setDestinationScreen
import com.pjtm23.explorer.presentation.showRoute.navigateToShowRoute
import com.pjtm23.explorer.presentation.showRoute.showRouteScreen

@Composable
fun ExplorerNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = setDestinationNavigationRoute
) {
    NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
    ) {
        setDestinationScreen { navController.navigateToShowRoute() }
        showRouteScreen()
    }
}

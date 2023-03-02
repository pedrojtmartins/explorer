package com.pjtm23.explorer.presentation.showRoute

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ShowRouteScreen(
        viewModel: ShowRouteViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    ShowRouteScreen(viewState)
}

@Composable
fun ShowRouteScreen(
        viewState: ShowRouteViewState,
        modifier: Modifier = Modifier
) {
    Text(
        text = "distance: ${viewState.distance}\n" +
               "targetBearing:  ${viewState.targetBearing}\n" +
               "currBearing:  ${viewState.currentBearing}"
    )
}
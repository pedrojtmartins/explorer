package com.pjtm23.explorer.presentation.showRoute

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.pjtm23.explorer.navigation.BindNavigationViewModel

@Composable
fun ShowRouteScreen(
        viewModel: ShowRouteViewModel = hiltViewModel(),
) {
    BindNavigationViewModel(viewModel)
}


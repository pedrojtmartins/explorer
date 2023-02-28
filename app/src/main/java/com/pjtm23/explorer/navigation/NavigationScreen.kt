package com.pjtm23.explorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun BindNavigationViewModel(
        viewModel: NavigationViewModel,
        onNavigationEvent: (NavigationEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvents
                .onEach { onNavigationEvent(it) }
                .launchIn(this)
    }
}

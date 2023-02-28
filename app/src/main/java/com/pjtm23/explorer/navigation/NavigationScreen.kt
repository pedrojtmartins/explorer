package com.pjtm23.explorer.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun BindNavigationViewModel(viewModel: NavigationViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        bindNavigationViewModel(viewModel, context, this)
    }
}

private fun bindNavigationViewModel(
        viewModel: NavigationViewModel,
        context: Context,
        scope: CoroutineScope
) {
    val navHost = findNavigationHost(context)
    viewModel.navigationEvents
            .onEach { navHost.onNavigationEvent(it) }
            .launchIn(scope)
}

private fun findNavigationHost(context: Context): NavigationHost {
    if (context is NavigationHost) {
        return context
    }

    if (context is ContextWrapper) {
        return findNavigationHost(context.baseContext)
    }

    throw IllegalStateException("Not called in the context of a NavigationHost")
}

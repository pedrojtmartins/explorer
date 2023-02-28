package com.pjtm23.explorer.navigation

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

abstract class NavigationCoordinator {

    private var _activity: ComponentActivity? = null
    protected val activity get() = requireNotNull(_activity) { "Activity not set" }

    private var _navController: NavController? = null
    protected val navController get() = requireNotNull(_navController) { "NavController not set" }

    abstract fun onNavigationEvent(event: NavigationEvent): Boolean

    protected fun bind(activity: ComponentActivity, navController: NavController) {
        _activity = activity
        _navController = navController

        activity.setupLifecycleObserver()
    }

    private fun ComponentActivity.setupLifecycleObserver() {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _activity = null
                _navController = null
            }
        })
    }
}

interface NavigationEvent
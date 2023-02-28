package com.pjtm23.explorer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pjtm23.explorer.navigation.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NavigationHost {

    @Inject
    override lateinit var coordinator: ExplorerCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { coordinator.NavHost(this@MainActivity) }
    }
}


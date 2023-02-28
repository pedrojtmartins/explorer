package com.pjtm23.explorer.presentation.setDestination

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pjtm23.explorer.navigation.BindNavigationViewModel
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent.DestinationSet
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Confirm
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.LatitudeUpdated
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.LongitudeUpdated
import com.pjtm23.explorer.presentation.theme.ExplorerTheme

private const val TAG = "SetDestinationScreen"

@Composable
fun SetDestinationScreen(
        onDestinationSet: () -> Unit,
        viewModel: SetDestinationViewModel = hiltViewModel()
) {
    BindNavigationViewModel(viewModel) {
        when (it as? SetDestinationNavigationEvent) {
            DestinationSet -> onDestinationSet()
            else -> Log.d(TAG, "Unknown event: $it")
        }
    }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    SetDestinationScreen(
            viewState = viewState,
            onLatitudeUpdated = { viewModel.onViewEvent(LatitudeUpdated(it)) },
            onLongitudeUpdated = { viewModel.onViewEvent(LongitudeUpdated(it)) },
            onConfirm = { viewModel.onViewEvent(Confirm) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetDestinationScreen(
        viewState: SetDestinationViewState,
        onLatitudeUpdated: (String) -> Unit,
        onLongitudeUpdated: (String) -> Unit,
        onConfirm: () -> Unit
) {
    Column {
        TextField(
                value = viewState.latitude,
                onValueChange = { value -> onLatitudeUpdated(value) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        TextField(
                value = viewState.longitude,
                onValueChange = { value -> onLongitudeUpdated(value) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Button(onClick = { onConfirm() }) {
            Text(text = "Confirm")
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ExplorerTheme {
        SetDestinationScreen(
                viewState = SetDestinationViewState("46.54381", "2.44683"),
                onLatitudeUpdated = {},
                onLongitudeUpdated = {},
                onConfirm = {})
    }
}

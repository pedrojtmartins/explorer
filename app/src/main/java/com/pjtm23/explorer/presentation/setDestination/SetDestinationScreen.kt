@file:OptIn(ExperimentalMaterial3Api::class)

package com.pjtm23.explorer.presentation.setDestination

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import com.pjtm23.explorer.R
import com.pjtm23.explorer.presentation.mapBox.MapboxForCompose
import com.pjtm23.explorer.presentation.setDestination.SetDestinationNavigationEvent.DestinationSet
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Accelerometer
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.AddMarker
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Confirm
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.Magnetometer
import com.pjtm23.explorer.presentation.setDestination.SetDestinationViewEvent.RemoveMarker
import com.pjtm23.explorer.presentation.theme.ExplorerTheme

@Composable
fun SetDestinationScreen(
        onDestinationSet: () -> Unit,
        viewModel: SetDestinationViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.navigation.collect {
            when (it) {
                DestinationSet -> onDestinationSet()
            }
        }
    }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    SetDestinationScreen(
            viewState = viewState,
            onConfirm = { viewModel.onViewEvent(Confirm) },
            addMarker = { viewModel.onViewEvent(AddMarker(it)) },
            removeMarker = { viewModel.onViewEvent(RemoveMarker(it)) },
            setAccAlpha = { viewModel.onViewEvent(Accelerometer(it)) },
            setMagAlpha = { viewModel.onViewEvent(Magnetometer(it)) }
    )
}

@Composable
private fun SetDestinationScreen(
        viewState: SetDestinationViewState,
        addMarker: (Point) -> Unit,
        removeMarker: (Point) -> Unit,
        setAccAlpha: (String) -> Unit,
        setMagAlpha: (String) -> Unit,
        onConfirm: () -> Unit,
        modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        // TODO For testing only. To be removed
        lpfTesting(viewState, setAccAlpha, focusManager, setMagAlpha)

        Box {
            MapboxForCompose(
                    modifier = Modifier.fillMaxSize(),
                    marker = viewState.marker,
                    addMarker = { addMarker(it) },
                    deleteMarker = { removeMarker(it) }
            )

            SmallFloatingActionButton(
                    modifier = Modifier
                            .align(BottomEnd)
                            .padding(16.dp),
                    shape = CircleShape,
                    onClick = { onConfirm() }
            ) {
                Image(
                        modifier = Modifier.rotate(45F),
                        painter = painterResource(id = R.drawable.current_position),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun lpfTesting(
        viewState: SetDestinationViewState,
        setAccAlpha: (String) -> Unit,
        focusManager: FocusManager,
        setMagAlpha: (String) -> Unit
) {
    Column {
        Text(text = "Acc_alpha: ")
        TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewState.accAlpha,
                onValueChange = setAccAlpha,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "Mag_alpha: ")
        TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewState.magAlpha,
                onValueChange = setMagAlpha,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ExplorerTheme {
        SetDestinationScreen(
                viewState = SetDestinationViewState(),
                onConfirm = {},
                addMarker = {},
                removeMarker = {},
                setAccAlpha = {},
                setMagAlpha = {})
    }
}

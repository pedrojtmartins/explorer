package com.pjtm23.explorer.presentation.showRoute

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pjtm23.explorer.R
import com.pjtm23.explorer.presentation.theme.ExplorerTheme

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
    Box(modifier = modifier) {
        Text(
                text = "distance: ${viewState.distance}\n" +
                        "targetBearing:  ${viewState.targetBearing}\n" +
                        "currBearing:  ${viewState.deviceBearing}\n" +
                        "offset:  ${viewState.bearingOffset}"
        )

        Image(
                modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationZ = viewState.bearingOffset.toFloat()
                        },
                painter = painterResource(id = R.drawable.arrow_upward),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = null
        )
    }
}

@Preview
@Composable
fun ShowRouteScreenPreview() {
    val viewState = ShowRouteViewState(12, 34, 56, 78)

    ExplorerTheme {
        ShowRouteScreen(viewState)
    }
}
package com.pjtm23.explorer.presentation.mapBox

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.pjtm23.explorer.R.drawable

private const val CIRCLE_RADIUS = 10.0

@Composable
fun MapboxForCompose(
    addMarker: (Point) -> Unit,
    deleteMarker: (Point) -> Unit,
    modifier: Modifier = Modifier,
    marker: Point? = null,
    zoom: Double = 16.0,
    style: String = Style.OUTDOORS,
) {
    val markerColor = MaterialTheme.colorScheme.primary.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            initMapViewForCompose(
                context = context,
                zoom = zoom,
                style = style,
                addMarker = addMarker,
                deleteMarker = deleteMarker
            )
        },
        update = { mapView ->
            mapView.annotationManager.deleteAll()
            marker?.let {
                val annotation = CircleAnnotationOptions()
                    .withPoint(it)
                    .withCircleColor(markerColor)
                    .withCircleRadius(CIRCLE_RADIUS)

                mapView.annotationManager.create(annotation)
            }
        })
}

private fun initMapViewForCompose(
    context: Context,
    zoom: Double,
    style: String,
    addMarker: (Point) -> Unit,
    deleteMarker: (Point) -> Unit
): MapViewForCompose {

    lateinit var listener: OnIndicatorPositionChangedListener

    val cameraOptions = CameraOptions.Builder().zoom(zoom).build()
    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = cameraOptions,
        styleUri = style
    )

    return MapViewForCompose(
        context = context,
        mapInitOptions = mapInitOptions
    ).apply {
        val mapBox = getMapboxMap()
        mapBox.addOnMapLongClickListener {
            addMarker(it)
            false
        }

        annotationManager.addLongClickListener {
            deleteMarker(it.point)
            true
        }

        val currentPosition = context.resources.getDrawable(drawable.current_position, null)
        location.apply {
            updateSettings {
                enabled = true
                locationPuck = LocationPuck2D(
                    bearingImage = currentPosition,
                    shadowImage = currentPosition,
                    scaleExpression = Expression.interpolate {
                        linear()
                        zoom()
                        stop {
                            literal(0.0)
                            literal(0.6)
                        }
                        stop {
                            literal(20.0)
                            literal(1.0)
                        }
                    }.toJson()
                )
            }

            listener = OnIndicatorPositionChangedListener { point ->
                mapBox.setCamera(CameraOptions.Builder().center(point).build())
                gestures.focalPoint = mapBox.pixelForCoordinate(point)
                removeOnIndicatorPositionChangedListener(listener)
            }
            addOnIndicatorPositionChangedListener(listener)
        }
    }
}

private class MapViewForCompose(
    context: Context,
    mapInitOptions: MapInitOptions = MapInitOptions(context)
) : MapView(context, mapInitOptions) {
    val annotationManager by lazy { annotations.createCircleAnnotationManager() }
}
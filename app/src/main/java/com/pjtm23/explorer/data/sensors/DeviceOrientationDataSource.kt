package com.pjtm23.explorer.data.sensors

import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.pjtm23.explorer.data.local.LowPassFilterDataSource
import com.pjtm23.explorer.di.SensorAccelerometer
import com.pjtm23.explorer.di.SensorMagnetometer
import com.pjtm23.explorer.domain.models.DeviceOrientation
import com.pjtm23.explorer.utils.filters.LowPassFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


private const val SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME

class DeviceOrientationDataSource @Inject constructor(
        private val sensorManager: SensorManager,
        @SensorAccelerometer private val accelerometer: Sensor?,
        @SensorMagnetometer private val magnetometer: Sensor?,
        private val lowPassFilter: LowPassFilter,
        lowPassFilterDataSource: LowPassFilterDataSource
) {

    private val accelerometerAlpha = lowPassFilterDataSource.getAccelerometerAlpha()
    private val magnetometerAlpha = lowPassFilterDataSource.getMagnetometerAlpha()

    private val accelerometerData = FloatArray(3)
    private val magnetometerData = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)

    operator fun invoke(): Flow<DeviceOrientation> = callbackFlow {
        if (accelerometer == null) return@callbackFlow
        if (magnetometer == null) return@callbackFlow

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                calculateDeviceOrientation(event)?.let { trySend(it) }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SENSOR_DELAY)
        sensorManager.registerListener(listener, magnetometer, SENSOR_DELAY)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    private fun calculateDeviceOrientation(event: SensorEvent?): DeviceOrientation? {
        when (event?.sensor?.type) {
            TYPE_ACCELEROMETER -> lowPassFilter.filter(
                event.values.clone(),
                accelerometerData,
                accelerometerAlpha
            )
            TYPE_MAGNETIC_FIELD -> lowPassFilter.filter(
                event.values.clone(),
                magnetometerData,
                magnetometerAlpha
            )

            else -> return null
        }

        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerData,
            magnetometerData
        )

        SensorManager.getOrientation(rotationMatrix, orientationValues)

        return DeviceOrientation(azimuth = Math.toDegrees(orientationValues[0].toDouble()))
    }
}



package com.pjtm23.explorer.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.pjtm23.explorer.di.SensorAccelerometer
import com.pjtm23.explorer.di.SensorMagnetometer
import com.pjtm23.explorer.domain.models.DeviceOrientation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DeviceOrientationDataSource @Inject constructor(
        private val sensorManager: SensorManager,
        @SensorAccelerometer private val accelerometer: Sensor?,
        @SensorMagnetometer private val magnetometer: Sensor?
) {

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)

    operator fun invoke(): Flow<DeviceOrientation> = callbackFlow {
        if (accelerometer == null) return@callbackFlow
        if (magnetometer == null) return@callbackFlow

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                calculateDeviceOrientation(event)?.let { trySend(it) }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    private fun calculateDeviceOrientation(event: SensorEvent?): DeviceOrientation? {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerData = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerData = event.values.clone()
            else -> return null
        }

        val rotationMatrix = FloatArray(9)
        val rotationOK = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerData,
            magnetometerData
        )

        val orientationValues = FloatArray(3)
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues)
        }

        return DeviceOrientation(
            azimuth = Math.toDegrees(orientationValues[0].toDouble()),
            pitch = Math.toDegrees(orientationValues[1].toDouble()),
            roll = Math.toDegrees(orientationValues[2].toDouble()),
        )
    }
}
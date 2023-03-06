package com.pjtm23.explorer.domain.models

data class DeviceOrientation(
        val azimuth: Double = 0.0,
        val pitch: Double = 0.0,
        val roll: Double = 0.0
) {

    fun isValid() = azimuth != 0.0 || pitch != 0.0 || roll != 0.0
}

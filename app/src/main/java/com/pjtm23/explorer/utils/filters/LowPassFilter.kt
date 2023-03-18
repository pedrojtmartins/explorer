package com.pjtm23.explorer.utils.filters

import javax.inject.Inject

class LowPassFilter @Inject constructor() {

    fun filter(
        input: FloatArray,
        output: FloatArray?,
        alpha: Float
    ): FloatArray {
        if (output == null || input.lastIndex > output.lastIndex) return input
        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
        return output
    }
}
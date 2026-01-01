package com.example.quakealert.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    context: Context,
    private val onShake: (Float) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Gravity components
    private var gravityX = 0f
    private var gravityY = 0f
    private var gravityZ = 0f

    private val alpha = 0.8f   // filter constant

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Remove gravity (high-pass filter)
        gravityX = alpha * gravityX + (1 - alpha) * x
        gravityY = alpha * gravityY + (1 - alpha) * y
        gravityZ = alpha * gravityZ + (1 - alpha) * z

        val linearX = x - gravityX
        val linearY = y - gravityY
        val linearZ = z - gravityZ

        val shakeMagnitude =
            sqrt(
                linearX * linearX +
                        linearY * linearY +
                        linearZ * linearZ
            )

        onShake(shakeMagnitude)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

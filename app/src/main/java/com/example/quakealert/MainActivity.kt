package com.example.quakealert

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quakealert.databinding.ActivityMainBinding
import com.example.quakealert.sensor.ShakeDetector
import com.example.quakealert.util.NotificationHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.max
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var shakeDetector: ShakeDetector

    private lateinit var currentSensor: TextView
    private lateinit var currentMagnitude: TextView
    private lateinit var shakeProgress: ProgressBar
    private lateinit var shakeValueText: TextView
    private lateinit var magnitudeChart: LineChart

    private val magnitudeEntries = mutableListOf<Entry>()
    private var alertCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentSensor = binding.currentSensor
        currentMagnitude = binding.currentMagnitude
        shakeProgress = binding.shakeProgress
        shakeValueText = binding.shakeValueText
        magnitudeChart = binding.magnitudeChart

        setupChart()

        binding.mapBtn.setOnClickListener { startActivity(Intent(this, MapActivity::class.java)) }
        binding.settingsBtn.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
        binding.emergencyBtn.setOnClickListener { startActivity(Intent(this, EmergencyActivity::class.java)) }

        shakeDetector = ShakeDetector(this) { shakeValue ->
            val prefs = getSharedPreferences("quake_prefs", MODE_PRIVATE)
            val alertsEnabled = prefs.getBoolean("alerts_enabled", true)
            val sensitivity = prefs.getInt("sensor_sensitivity", 10)

            val scaledShake = (shakeValue * 10).toInt()

            runOnUiThread {
                shakeProgress.progress = scaledShake
                shakeValueText.text = "Current Shake: $scaledShake / Threshold: $sensitivity"
            }

            if (alertsEnabled && scaledShake > sensitivity) {
                NotificationHelper.send(this, "Device shake detected!")
                recordMagnitude(scaledShake)
            }
        }

        updateThresholdText()
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
        updateThresholdText()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    private fun updateThresholdText() {
        val prefs = getSharedPreferences("quake_prefs", MODE_PRIVATE)
        val sensor = prefs.getInt("sensor_sensitivity", 10)
        val magnitude = prefs.getInt("magnitude_threshold", 4)

        currentSensor.text = "Sensor Sensitivity: $sensor"
        currentMagnitude.text = "Magnitude Threshold: ≥ $magnitude"
    }

    private fun setupChart() {
        magnitudeChart.description.isEnabled = false
        magnitudeChart.axisRight.isEnabled = false
        magnitudeChart.data = LineData()
    }

    private fun recordMagnitude(magnitude: Int) {
        val data = magnitudeChart.data ?: LineData().also { magnitudeChart.data = it }
        val set = data.getDataSetByIndex(0) as? LineDataSet ?: LineDataSet(magnitudeEntries, "Shake Magnitude").also {
            it.lineWidth = 2f
            it.circleRadius = 4f
            it.setDrawValues(false)
            data.addDataSet(it)
        }

        val entry = Entry(alertCount.toFloat(), magnitude.toFloat())
        magnitudeEntries.add(entry)
        set.addEntry(entry)
        data.notifyDataChanged()
        magnitudeChart.notifyDataSetChanged()
        magnitudeChart.invalidate()
        alertCount++
    }
}

package com.example.quakealert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quakealert.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val prefsName = "quake_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        val sensorValue = prefs.getInt("sensor_sensitivity", 10)
        val magnitudeValue = prefs.getInt("magnitude_threshold", 4)
        val alertsEnabled = prefs.getBoolean("alerts_enabled", true)

        binding.sensorSeekBar.value = sensorValue.toFloat()
        binding.magnitudeSeekBar.value = magnitudeValue.toFloat()
        binding.alertsSwitch.isChecked = alertsEnabled

        binding.sensorValue.text = sensorValue.toString()
        binding.magnitudeValue.text = magnitudeValue.toString()

        binding.sensorSeekBar.addOnChangeListener { _, value, _ ->
            binding.sensorValue.text = value.toInt().toString()
            prefs.edit().putInt("sensor_sensitivity", value.toInt()).apply()
        }

        binding.magnitudeSeekBar.addOnChangeListener { _, value, _ ->
            binding.magnitudeValue.text = value.toInt().toString()
            prefs.edit().putInt("magnitude_threshold", value.toInt()).apply()
        }

        binding.alertsSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("alerts_enabled", isChecked).apply()
        }
    }
}

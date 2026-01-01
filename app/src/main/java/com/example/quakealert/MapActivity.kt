package com.example.quakealert

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.quakealert.network.RetrofitClient
import com.example.quakealert.sensor.ShakeDetector
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var shakeDetector: ShakeDetector

    private var currentLocation: Location? = null
    private var userMarker: Marker? = null
    private var shakeMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(2.0)
        map.controller.setCenter(GeoPoint(20.0, 0.0)) // World view initially

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestLocationUpdates()
        loadEarthquakeMarkers()

        shakeDetector = ShakeDetector(this) { shakeValue: Float ->
            currentLocation?.let {
                showShakeMarker(it.latitude, it.longitude, shakeValue.toInt())
            }
        }
    }

    // ================= USER LOCATION =================
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        val listener = LocationListener { location ->
            currentLocation = location
            showUserMarker(location.latitude, location.longitude)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, listener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5f, listener)

        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            currentLocation = it
            showUserMarker(it.latitude, it.longitude)
        }
    }

    private fun showUserMarker(lat: Double, lon: Double) {
        val point = GeoPoint(lat, lon)
        userMarker?.let { map.overlays.remove(it) }

        userMarker = Marker(map).apply {
            position = point
            title = "📍 You are here"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }

        map.overlays.add(userMarker)
        map.invalidate()
    }

    // ================= SHAKE LOCATION =================
    private fun showShakeMarker(lat: Double, lon: Double, value: Int) {
        val point = GeoPoint(lat, lon)
        shakeMarker?.let { map.overlays.remove(it) }

        shakeMarker = Marker(map).apply {
            position = point
            title = "⚠️ Shake detected ($value)"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }

        map.overlays.add(shakeMarker)
        map.invalidate()
    }

    // ================= EARTHQUAKE API =================
    private fun loadEarthquakeMarkers() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getEarthquakes()
                println("API response received: ${response.features.size} earthquakes") // debug

                response.features.forEach { feature ->
                    val coords = feature.geometry.coordinates
                    if (coords.size < 2) return@forEach

                    val lat = coords[1]
                    val lon = coords[0]
                    val mag = feature.properties.mag ?: 0.0
                    val place = feature.properties.place ?: "Unknown"

                    // Use OSMDroid default marker (no need for drawable)
                    val marker = Marker(map).apply {
                        position = GeoPoint(lat, lon)
                        title = "🌍 M$mag - $place"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        // no icon assignment → default marker
                    }

                    map.overlays.add(marker)
                }

                map.invalidate()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to load earthquake markers: ${e.message}")
            }
        }
    }



    override fun onResume() {
        super.onResume()
        map.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        shakeDetector.stop()
    }
}

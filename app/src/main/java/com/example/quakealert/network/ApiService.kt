package com.example.quakealert.network

import retrofit2.http.GET


interface ApiService {
    @GET("earthquakes/feed/v1.0/summary/2.5_day.geojson")
    suspend fun getEarthquakes(): Map<String, Any>
}
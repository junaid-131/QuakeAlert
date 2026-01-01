package com.example.quakealert.data

import retrofit2.http.GET

interface EarthquakeApi {
    @GET("feed/v1.0/summary/all_day.geojson")
    suspend fun getEarthquakes(): EarthquakeResponse
}

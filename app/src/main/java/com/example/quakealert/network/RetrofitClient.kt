package com.example.quakealert.network

import com.example.quakealert.data.EarthquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val api: EarthquakeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EarthquakeApi::class.java)
    }
}

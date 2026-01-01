package com.example.quakealert.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Earthquake(
    @PrimaryKey val id: String,
    val magnitude: Double,
    val place: String,
    val time: Long,
    val lat: Double,
    val lon: Double
)

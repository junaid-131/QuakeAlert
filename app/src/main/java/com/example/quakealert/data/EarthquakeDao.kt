package com.example.quakealert.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface EarthquakeDao {
    @Insert
    suspend fun insertEarthquake(eq: Earthquake)


    @Insert
    suspend fun insertShake(event: ShakeEvent)


    @Query("SELECT * FROM Earthquake")
    suspend fun getAll(): List<Earthquake>
}
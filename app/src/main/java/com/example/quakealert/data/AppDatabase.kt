package com.example.quakealert.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Earthquake::class, ShakeEvent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): EarthquakeDao


    companion object {
        fun get(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "quake_db"
        ).build()
    }
}
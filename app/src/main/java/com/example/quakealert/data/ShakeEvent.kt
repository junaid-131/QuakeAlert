package com.example.quakealert.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ShakeEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long
)

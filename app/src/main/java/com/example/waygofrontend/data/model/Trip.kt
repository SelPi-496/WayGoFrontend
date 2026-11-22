package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey val id: String,
    val destinationId: String,
    val range: String,
    val days: Int,
    val status: String,
    val userId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

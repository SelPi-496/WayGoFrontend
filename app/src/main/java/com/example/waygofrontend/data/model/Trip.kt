package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey val id: String,
    val firebaseId: String? = null,
    val destinationId: String,
    val range: String,
    val days: Int,
    val status: String,
    val userId: String? = null,
    val syncedAt: Long = 0,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

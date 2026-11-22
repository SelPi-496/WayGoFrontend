package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Booking(
    @PrimaryKey val id: String,
    val tripId: String,
    val service: String,
    val price: String,
    val status: String,
    val createdAt: Long = System.currentTimeMillis()
)

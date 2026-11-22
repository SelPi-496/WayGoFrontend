package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinations")
data class Destination(
    @PrimaryKey val id: String,
    val name: String,
    val place: String,
    val rating: String,
    val price: String,
    val match: String,
    val reason: String,
    val topLabel: String,
    val imageUrl: String? = null
)

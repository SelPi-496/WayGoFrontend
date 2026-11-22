package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val firebaseUid: String? = null,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val syncedAt: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)

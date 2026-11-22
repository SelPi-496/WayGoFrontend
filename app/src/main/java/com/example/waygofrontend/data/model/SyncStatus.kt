package com.example.waygofrontend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_status", primaryKeys = ["tableName", "recordId"])
data class SyncStatus(
    val tableName: String,
    val recordId: String,
    val lastSyncedAt: Long = 0,
    val status: String = "PENDING"
)

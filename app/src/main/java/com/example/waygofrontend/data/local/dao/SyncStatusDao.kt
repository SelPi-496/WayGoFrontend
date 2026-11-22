package com.example.waygofrontend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.waygofrontend.data.model.SyncStatus

@Dao
interface SyncStatusDao {

    @Query("SELECT * FROM sync_status WHERE tableName = :tableName AND recordId = :recordId")
    suspend fun getSyncStatus(tableName: String, recordId: String): SyncStatus?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncStatus(syncStatus: SyncStatus)

    @Update
    suspend fun updateSyncStatus(syncStatus: SyncStatus)
}

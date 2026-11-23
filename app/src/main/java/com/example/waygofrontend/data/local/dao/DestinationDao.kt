package com.example.waygofrontend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.waygofrontend.data.model.Destination
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {

    @Query("SELECT * FROM destinations ORDER BY name")
    fun getAll(): Flow<List<Destination>>

    @Query("SELECT * FROM destinations WHERE id = :id")
    fun getById(id: String): Flow<Destination?>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAll(destinations: List<Destination>)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(destination: Destination)

    @Delete
    suspend fun delete(destination: Destination)

    @Query("DELETE FROM destinations")
    suspend fun deleteAll()
}

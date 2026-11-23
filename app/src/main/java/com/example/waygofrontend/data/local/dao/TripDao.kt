package com.example.waygofrontend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.waygofrontend.data.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: String): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getById(id: String): Flow<Trip?>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(trip: Trip)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<Trip>)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("DELETE FROM trips WHERE userId = :userId")
    suspend fun deleteByUser(userId: String)
}

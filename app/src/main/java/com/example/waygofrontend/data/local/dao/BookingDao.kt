package com.example.waygofrontend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.waygofrontend.data.model.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getByTrip(tripId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    fun getById(id: String): Flow<Booking?>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(booking: Booking)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookings: List<Booking>)

    @Delete
    suspend fun delete(booking: Booking)
}

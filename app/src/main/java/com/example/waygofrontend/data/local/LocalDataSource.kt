package com.example.waygofrontend.data.local

import android.content.Context
import com.example.waygofrontend.data.local.dao.BookingDao
import com.example.waygofrontend.data.local.dao.DestinationDao
import com.example.waygofrontend.data.local.dao.SyncStatusDao
import com.example.waygofrontend.data.local.dao.TripDao
import com.example.waygofrontend.data.local.dao.UserDao
import com.example.waygofrontend.data.model.Booking
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.SyncStatus
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.data.model.User
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val destinationDao: DestinationDao,
    private val tripDao: TripDao,
    private val userDao: UserDao,
    private val bookingDao: BookingDao,
    private val syncStatusDao: SyncStatusDao
) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(context: Context): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                LocalDataSource(
                    db.destinationDao(),
                    db.tripDao(),
                    db.userDao(),
                    db.bookingDao(),
                    db.syncStatusDao()
                ).also { INSTANCE = it }
            }
        }
    }

    // Destinations
    fun getDestinations(): Flow<List<Destination>> = destinationDao.getAll()
    fun getDestinationById(id: String): Flow<Destination?> = destinationDao.getById(id)
    suspend fun insertDestinations(destinations: List<Destination>) = destinationDao.insertAll(destinations)
    suspend fun insertDestination(destination: Destination) = destinationDao.insert(destination)
    suspend fun deleteAllDestinations() = destinationDao.deleteAll()

    // Trips
    fun getTrips(): Flow<List<Trip>> = tripDao.getAll()
    fun getTripsByUser(userId: String): Flow<List<Trip>> = tripDao.getByUser(userId)
    fun getTripById(id: String): Flow<Trip?> = tripDao.getById(id)
    suspend fun insertTrip(trip: Trip) = tripDao.insert(trip)
    suspend fun insertTrips(trips: List<Trip>) = tripDao.insertAll(trips)
    suspend fun updateTrip(trip: Trip) = tripDao.update(trip)
    suspend fun deleteTrip(trip: Trip) = tripDao.delete(trip)
    suspend fun deleteTripsByUser(userId: String) = tripDao.deleteByUser(userId)

    // Users
    fun getUserById(id: String): Flow<User?> = userDao.getById(id)
    fun getUserByEmail(email: String): Flow<User?> = userDao.getByEmail(email)
    suspend fun insertUser(user: User) = userDao.insert(user)
    suspend fun updateUser(user: User) = userDao.update(user)

    // Bookings
    fun getBookings(): Flow<List<Booking>> = bookingDao.getAll()
    fun getBookingsByTrip(tripId: String): Flow<List<Booking>> = bookingDao.getByTrip(tripId)
    suspend fun insertBooking(booking: Booking) = bookingDao.insert(booking)
    suspend fun insertBookings(bookings: List<Booking>) = bookingDao.insertAll(bookings)

    // Sync Status
    suspend fun getSyncStatus(tableName: String, recordId: String): SyncStatus? =
        syncStatusDao.getSyncStatus(tableName, recordId)

    suspend fun markSynced(tableName: String, recordId: String) =
        syncStatusDao.insertSyncStatus(
            SyncStatus(
                tableName = tableName,
                recordId = recordId,
                status = "SYNCED",
                lastSyncedAt = System.currentTimeMillis()
            )
        )

    suspend fun markPending(tableName: String, recordId: String) =
        syncStatusDao.insertSyncStatus(
            SyncStatus(
                tableName = tableName,
                recordId = recordId,
                status = "PENDING",
                lastSyncedAt = 0
            )
        )
}

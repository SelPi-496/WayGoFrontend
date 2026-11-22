package com.example.waygofrontend.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.waygofrontend.data.local.dao.BookingDao
import com.example.waygofrontend.data.local.dao.DestinationDao
import com.example.waygofrontend.data.local.dao.TripDao
import com.example.waygofrontend.data.local.dao.UserDao
import com.example.waygofrontend.data.model.Booking
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.data.model.User

@Database(
    entities = [Destination::class, Trip::class, User::class, Booking::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "waygo_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

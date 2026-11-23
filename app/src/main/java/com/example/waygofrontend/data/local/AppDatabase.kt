package com.example.waygofrontend.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.waygofrontend.data.WaygoData
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

@Database(
    entities = [
        User::class,
        Trip::class,
        Destination::class,
        Booking::class,
        SyncStatus::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun destinationDao(): DestinationDao
    abstract fun bookingDao(): BookingDao
    abstract fun syncStatusDao(): SyncStatusDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN firebaseUid TEXT")
                database.execSQL("ALTER TABLE users ADD COLUMN syncedAt INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE trips ADD COLUMN firebaseId TEXT")
                database.execSQL("ALTER TABLE trips ADD COLUMN syncedAt INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE trips ADD COLUMN isDeleted INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE bookings ADD COLUMN firebaseId TEXT")
                database.execSQL("ALTER TABLE bookings ADD COLUMN syncedAt INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE bookings ADD COLUMN isDeleted INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE destinations ADD COLUMN firebaseId TEXT")
                database.execSQL("ALTER TABLE destinations ADD COLUMN syncedAt INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE destinations ADD COLUMN isDeleted INTEGER DEFAULT 0")

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS sync_status (
                        tableName TEXT NOT NULL,
                        recordId TEXT NOT NULL,
                        lastSyncedAt INTEGER NOT NULL DEFAULT 0,
                        status TEXT NOT NULL DEFAULT 'PENDING',
                        PRIMARY KEY (tableName, recordId)
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "waygo_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            WaygoData.destinations.forEach { d ->
                                val values = ContentValues().apply {
                                    put("id", d.id)
                                    put("name", d.name)
                                    put("place", d.place)
                                    put("rating", d.rating)
                                    put("price", d.price)
                                    put("match", d.match)
                                    put("reason", d.reason)
                                    put("topLabel", d.topLabel)
                                    put("syncedAt", 0L)
                                    put("isDeleted", 0)
                                }
                                db.insert("destinations", SQLiteDatabase.CONFLICT_REPLACE, values)
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

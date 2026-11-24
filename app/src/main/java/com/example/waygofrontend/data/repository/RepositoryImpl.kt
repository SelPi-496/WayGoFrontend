package com.example.waygofrontend.data.repository

import com.example.waygofrontend.data.firebase.FirebaseAuthHelper
import com.example.waygofrontend.data.local.LocalDataSource
import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Booking
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.data.model.User
import com.example.waygofrontend.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import com.google.android.gms.tasks.Tasks

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val firebaseAuthHelper: FirebaseAuthHelper
) : DestinationRepository,
    TripRepository,
    UserRepository,
    BookingRepository {

    // --- Destinations ---
    override fun getDestinations(): Flow<List<Destination>> = localDataSource.getDestinations()

    override fun getDestinationById(id: String): Flow<Destination?> = localDataSource.getDestinationById(id)

    override suspend fun refreshDestinations(): ApiResponse<List<Destination>> {
        val result = remoteDataSource.getDestinations()
        if (result.success && result.data != null) {
            localDataSource.insertDestinations(result.data)
        }
        return result
    }

    override suspend fun getDestinationRemote(id: String): ApiResponse<Destination> {
        return remoteDataSource.getDestination(id)
    }

    // --- Trips ---
    override fun getTrips(): Flow<List<Trip>> = localDataSource.getTrips()

    override fun getTripsByUser(userId: String): Flow<List<Trip>> = localDataSource.getTripsByUser(userId)

    override fun getTripById(id: String): Flow<Trip?> = localDataSource.getTripById(id)

    override suspend fun refreshTrips(userId: String?): ApiResponse<List<Trip>> {
        val result = remoteDataSource.getTrips(userId)
        if (result.success && result.data != null) {
            localDataSource.insertTrips(result.data)
        }
        return result
    }

    override suspend fun createTrip(trip: Trip): ApiResponse<Trip> {
        val result = remoteDataSource.createTrip(trip)
        if (result.success && result.data != null) {
            localDataSource.insertTrip(result.data!!)
        }
        return result
    }

    override suspend fun updateTrip(trip: Trip): ApiResponse<Trip> {
        val result = remoteDataSource.updateTrip(trip.id, trip)
        if (result.success) {
            localDataSource.updateTrip(trip)
        }
        return result
    }

    override suspend fun deleteTrip(id: String): ApiResponse<Void> {
        return remoteDataSource.deleteTrip(id)
    }

    // --- Users ---
    override fun getUserById(id: String): Flow<User?> = localDataSource.getUserById(id)

    override fun getUserByEmail(email: String): Flow<User?> = localDataSource.getUserByEmail(email)

    override suspend fun login(email: String, password: String): ApiResponse<User> = withContext(Dispatchers.IO) {
        val result: ApiResponse<User> = try {
            val authResult = Tasks.await(firebaseAuthHelper.signIn(email, password))
            val user = firebaseAuthHelper.toUserModel(authResult.user)
            if (user == null) {
                ApiResponse(null, "User data is null", false)
            } else {
                localDataSource.insertUser(user)
                ApiResponse(user, "Đăng nhập thành công", true)
            }
        } catch (e: Exception) {
            ApiResponse(null, "Đăng nhập thất bại: ${e.message}", false)
        }
        return@withContext result
    }

    override suspend fun register(email: String, password: String, displayName: String?): ApiResponse<User> = withContext(Dispatchers.IO) {
        val result: ApiResponse<User> = try {
            val authResult = Tasks.await(firebaseAuthHelper.signUp(email, password, displayName))
            val user = firebaseAuthHelper.toUserModel(authResult.user)
            if (user == null) {
                ApiResponse(null, "User data is null", false)
            } else {
                localDataSource.insertUser(user)
                ApiResponse(user, "Đăng ký thành công", true)
            }
        } catch (e: Exception) {
            ApiResponse(null, "Đăng ký thất bại: ${e.message}", false)
        }
        return@withContext result
    }

    override suspend fun getUserRemote(id: String): ApiResponse<User> {
        return remoteDataSource.getUser(id)
    }

    // --- Bookings ---
    override fun getBookingsByTrip(tripId: String): Flow<List<Booking>> = localDataSource.getBookingsByTrip(tripId)

    override suspend fun createBooking(booking: Booking): ApiResponse<Booking> {
        val result = remoteDataSource.createBooking(booking)
        if (result.success && result.data != null) {
            localDataSource.insertBooking(result.data!!)
        }
        return result
    }

    override suspend fun getBookingsByTripRemote(tripId: String): ApiResponse<List<Booking>> {
        return remoteDataSource.getBookings(tripId)
    }
}

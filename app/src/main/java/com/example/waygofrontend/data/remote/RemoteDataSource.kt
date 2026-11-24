package com.example.waygofrontend.data.remote

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Booking
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.data.model.User
import com.example.waygofrontend.data.remote.api.WaygoApi
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class RemoteDataSource(private val api: WaygoApi) {

    // Destinations
    suspend fun getDestinations(): ApiResponse<List<Destination>> = call { api.getDestinations() }
    suspend fun getDestination(id: String): ApiResponse<Destination> = call { api.getDestination(id) }

    // Trips
    suspend fun getTrips(userId: String?): ApiResponse<List<Trip>> = call { api.getTrips(userId) }
    suspend fun getTrip(id: String): ApiResponse<Trip> = call { api.getTrip(id) }
    suspend fun createTrip(trip: Trip): ApiResponse<Trip> = call { api.createTrip(trip) }
    suspend fun updateTrip(id: String, trip: Trip): ApiResponse<Trip> = call { api.updateTrip(id, trip) }
    suspend fun deleteTrip(id: String): ApiResponse<Void> = call { api.deleteTrip(id) }

    // Users
    suspend fun getUser(id: String): ApiResponse<User> = call { api.getUser(id) }

    // Bookings
    suspend fun createBooking(booking: Booking): ApiResponse<Booking> = call { api.createBooking(booking) }
    suspend fun getBookings(tripId: String): ApiResponse<List<Booking>> = call { api.getBookings(tripId) }

    private suspend fun <T> call(block: () -> retrofit2.Call<ApiResponse<T>>): ApiResponse<T> = withContext(Dispatchers.IO) {
        try {
            val response = block().execute()
            if (response.isSuccessful) {
                response.body() ?: ApiResponse(null, "Empty response", false)
            } else {
                ApiResponse(null, "Error ${response.code()}: ${response.message()}", false)
            }
        } catch (e: IOException) {
            ApiResponse(null, "Network error: ${e.message}", false)
        } catch (e: Exception) {
            ApiResponse(null, "Unexpected error: ${e.message}", false)
        }
    }
}

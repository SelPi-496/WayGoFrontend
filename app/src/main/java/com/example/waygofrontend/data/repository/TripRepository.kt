package com.example.waygofrontend.data.repository

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getTrips(): Flow<List<Trip>>
    fun getTripsByUser(userId: String): Flow<List<Trip>>
    fun getTripById(id: String): Flow<Trip?>
    suspend fun refreshTrips(userId: String?): ApiResponse<List<Trip>>
    suspend fun createTrip(trip: Trip): ApiResponse<Trip>
    suspend fun updateTrip(trip: Trip): ApiResponse<Trip>
    suspend fun deleteTrip(id: String): ApiResponse<Void>
}

package com.example.waygofrontend.data.repository

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {

    fun getBookingsByTrip(tripId: String): Flow<List<Booking>>
    suspend fun createBooking(booking: Booking): ApiResponse<Booking>
    suspend fun getBookingsByTripRemote(tripId: String): ApiResponse<List<Booking>>
}

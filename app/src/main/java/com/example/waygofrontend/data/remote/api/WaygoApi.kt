package com.example.waygofrontend.data.remote.api

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Booking
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WaygoApi {

    // Destinations
    @GET("destinations")
    fun getDestinations(): Call<ApiResponse<List<Destination>>>

    @GET("destinations/{id}")
    fun getDestination(@Path("id") id: String): Call<ApiResponse<Destination>>

    // Trips
    @GET("trips")
    fun getTrips(@Query("userId") userId: String?): Call<ApiResponse<List<Trip>>>

    @GET("trips/{id}")
    fun getTrip(@Path("id") id: String): Call<ApiResponse<Trip>>

    @POST("trips")
    fun createTrip(@Body trip: Trip): Call<ApiResponse<Trip>>

    @PUT("trips/{id}")
    fun updateTrip(@Path("id") id: String, @Body trip: Trip): Call<ApiResponse<Trip>>

    @DELETE("trips/{id}")
    fun deleteTrip(@Path("id") id: String): Call<ApiResponse<Void>>

    // Users
    @GET("users/{id}")
    fun getUser(@Path("id") id: String): Call<ApiResponse<User>>

    // Auth
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<ApiResponse<AuthResponse>>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<ApiResponse<AuthResponse>>

    // Bookings
    @POST("bookings")
    fun createBooking(@Body booking: Booking): Call<ApiResponse<Booking>>

    @GET("bookings")
    fun getBookings(@Query("tripId") tripId: String): Call<ApiResponse<List<Booking>>>
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String?
)

data class AuthResponse(
    val user: User,
    val token: String
)

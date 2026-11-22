package com.example.waygofrontend.data.repository

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.data.model.Trip
import kotlinx.coroutines.flow.Flow

interface DestinationRepository {

    fun getDestinations(): Flow<List<Destination>>
    fun getDestinationById(id: String): Flow<Destination?>
    suspend fun refreshDestinations(): ApiResponse<List<Destination>>
    suspend fun getDestinationRemote(id: String): ApiResponse<Destination>
}

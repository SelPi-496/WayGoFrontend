package com.example.waygofrontend.data.repository

import com.example.waygofrontend.data.model.ApiResponse
import com.example.waygofrontend.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserById(id: String): Flow<User?>
    fun getUserByEmail(email: String): Flow<User?>
    suspend fun login(email: String, password: String): ApiResponse<User>
    suspend fun register(email: String, password: String, displayName: String?): ApiResponse<User>
    suspend fun getUserRemote(id: String): ApiResponse<User>
}

package com.example.waygofrontend.data.model

data class ApiResponse<T>(
    val data: T?,
    val message: String?,
    val success: Boolean
)

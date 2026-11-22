package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Booking

data class BookingUiState(
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

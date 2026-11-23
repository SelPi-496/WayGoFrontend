package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Trip

data class TripsUiState(
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Trip

data class TripPlannerUiState(
    val selectedTrip: Trip? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

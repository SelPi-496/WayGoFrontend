package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Destination

data class DestinationsUiState(
    val destinations: List<Destination> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

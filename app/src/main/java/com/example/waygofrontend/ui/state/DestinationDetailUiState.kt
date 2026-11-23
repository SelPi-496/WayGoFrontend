package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Destination

data class DestinationDetailUiState(
    val destination: Destination? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.Category
import com.example.waygofrontend.data.model.Destination

data class ExploreUiState(
    val destinations: List<Destination> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.ui.state.TripPlannerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItineraryViewModel : ViewModel() {

    private val _state = MutableStateFlow(TripPlannerUiState())
    val state: StateFlow<TripPlannerUiState> = _state.asStateFlow()
}

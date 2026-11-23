package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.TripRepository
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.ui.state.TripPlannerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class TripPlannerViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TripPlannerUiState())
    val state: StateFlow<TripPlannerUiState> = _state.asStateFlow()

    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = tripRepository.createTrip(trip)
            if (result.success) {
                result.data?.let { created ->
                    _state.value = _state.value.copy(
                        selectedTrip = created,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _state.value = _state.value.copy(isLoading = false, error = result.message)
            }
        }
    }
}

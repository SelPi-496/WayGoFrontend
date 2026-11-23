package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.TripRepository
import com.example.waygofrontend.ui.state.TripsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class TripsViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TripsUiState())
    val state: StateFlow<TripsUiState> = _state.asStateFlow()

    fun loadTrips(userId: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val trips = if (userId != null) {
                    tripRepository.getTripsByUser(userId).first()
                } else {
                    tripRepository.getTrips().first()
                }
                _state.value = _state.value.copy(trips = trips, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun refreshTrips(userId: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = tripRepository.refreshTrips(userId)
            if (result.success) {
                _state.value = _state.value.copy(
                    trips = result.data ?: emptyList(),
                    isLoading = false,
                    error = null
                )
            } else {
                _state.value = _state.value.copy(isLoading = false, error = result.message)
            }
        }
    }
}

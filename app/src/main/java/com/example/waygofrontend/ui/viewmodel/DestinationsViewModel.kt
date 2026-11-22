package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.DestinationRepository
import com.example.waygofrontend.ui.state.DestinationsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class DestinationsViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DestinationsUiState())
    val state: StateFlow<DestinationsUiState> = _state.asStateFlow()

    fun loadDestinations() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val destinations = destinationRepository.getDestinations().first()
                _state.value = _state.value.copy(destinations = destinations, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

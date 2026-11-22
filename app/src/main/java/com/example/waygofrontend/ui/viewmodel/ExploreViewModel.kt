package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.DestinationRepository
import com.example.waygofrontend.ui.state.ExploreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class ExploreViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreUiState())
    val state: StateFlow<ExploreUiState> = _state.asStateFlow()

    fun loadDestinations() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val flow = destinationRepository.getDestinations()
            val first = flow.first()
            _state.value = _state.value.copy(
                destinations = first,
                isLoading = false,
                error = null
            )
        }
    }

    fun searchDestinations(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        val filtered = if (query.isBlank()) {
            _state.value.destinations
        } else {
            _state.value.destinations.filter {
                it.name.lowercase().contains(query.lowercase())
            }
        }
        _state.value = _state.value.copy(destinations = filtered)
    }
}

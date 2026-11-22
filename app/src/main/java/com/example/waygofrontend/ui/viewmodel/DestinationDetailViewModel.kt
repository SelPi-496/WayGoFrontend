package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.DestinationRepository
import com.example.waygofrontend.ui.state.DestinationDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class DestinationDetailViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DestinationDetailUiState())
    val state: StateFlow<DestinationDetailUiState> = _state.asStateFlow()

    fun loadDestination(destinationId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val destination = destinationRepository.getDestinationById(destinationId).first()
                _state.value = _state.value.copy(destination = destination, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

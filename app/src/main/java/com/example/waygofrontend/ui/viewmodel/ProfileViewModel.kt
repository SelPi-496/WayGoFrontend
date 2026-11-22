package com.example.waygofrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.waygofrontend.data.repository.UserRepository
import com.example.waygofrontend.ui.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val user = userRepository.getUserById(userId).first()
                _state.value = _state.value.copy(user = user, isLoading = false, isLoggedIn = user != null)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = userRepository.login(email, password)
            if (result.success) {
                result.data?.let { user ->
                    _state.value = _state.value.copy(
                        user = user,
                        isLoggedIn = true,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _state.value = _state.value.copy(isLoading = false, error = result.message)
            }
        }
    }

    fun register(email: String, password: String, displayName: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = userRepository.register(email, password, displayName)
            if (result.success) {
                result.data?.let { user ->
                    _state.value = _state.value.copy(
                        user = user,
                        isLoggedIn = true,
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

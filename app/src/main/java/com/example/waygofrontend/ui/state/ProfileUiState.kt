package com.example.waygofrontend.ui.state

import com.example.waygofrontend.data.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

package com.example.waygofrontend.ui.state

sealed class BaseUiState {
    data class Loading(val message: String = "Đang tải...") : BaseUiState()
    data class Success<T>(val data: T) : BaseUiState()
    data class Error(val message: String, val throwable: Throwable? = null) : BaseUiState()
}

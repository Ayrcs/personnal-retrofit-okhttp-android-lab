package com.example.learnretrofitokhttp.feature.auth


data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isInitializing: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: AuthError? = null
)

enum class AuthError {
    EMPTY_FIELDS,
    INVALID_CREDENTIALS,
    SESSION_EXPIRED,
    NETWORK,
    SERVER
}

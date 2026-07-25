package com.example.learnretrofitokhttp.feature.auth


data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: AuthError? = null
)

enum class AuthError {
    EMPTY_FIELDS,
    INVALID_CREDENTIALS,
    NETWORK,
    SERVER
}
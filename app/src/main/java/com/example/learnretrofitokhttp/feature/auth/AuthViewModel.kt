package com.example.learnretrofitokhttp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnretrofitokhttp.data.repository.AuthRepository
import com.example.learnretrofitokhttp.data.repository.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    // Un StateFlow est un objet Kotlin qui :
    // • contient toujours une valeur actuelle ;
    // • permet de lire cette valeur immédiatement ;
    // • avertit ses observateurs lorsque la valeur change ;
    // • transmet immédiatement la dernière valeur à un nouvel observateur.

    // C'est comme une variable classique sauf qu'elle prévient les observateurs quand elle change.
    // uiState.value contient la valeur de celle expose : une instance AuthUiState()

    // MutableStateFlow : est modifiable.
    // StateFlow : n'est pas modifiable.
    // .asStateFlow() masque les opérations de modifications.

    private val _uiState = MutableStateFlow(AuthUiState(
        isAuthenticated = authRepository.isAuthenticated()
    )) // Peut utiliser .update { }
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow() // Impossible à modifier

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                error = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                error = null
            )
        }
    }

    fun login() {
        val currentState = _uiState.value

        // Ne pas doubler la connexion
        if (currentState.isLoading) return

        // Afficher error si login/password n'est pas complet
        if (
            currentState.email.isBlank() ||
            currentState.password.isBlank()
        ) {
            _uiState.update {
                it.copy(error = AuthError.EMPTY_FIELDS)
            }
            return
        }

        // Si tout est bon
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }

        // Appel de l'api en coroutine
        viewModelScope.launch {
            val result = authRepository.login(
                email = currentState.email,
                password = currentState.password
            )

            _uiState.update { state ->
                when (result) {
                    LoginResult.SUCCESS -> state.copy(
                        password = "",
                        isLoading = false,
                        isAuthenticated = true,
                        error = null
                    )

                    LoginResult.INVALID_CREDENTIALS -> state.copy(
                        isLoading = false,
                        error = AuthError.INVALID_CREDENTIALS
                    )

                    LoginResult.NETWORK_ERROR -> state.copy(
                        isLoading = false,
                        error = AuthError.NETWORK
                    )

                    LoginResult.SERVER_ERROR -> state.copy(
                        isLoading = false,
                        error = AuthError.SERVER
                    )
                }
            }
        }
    }

    fun logout() {
        val currentState = _uiState.value

        if (currentState.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            authRepository.logout()

            _uiState.value = AuthUiState()
        }
    }
}
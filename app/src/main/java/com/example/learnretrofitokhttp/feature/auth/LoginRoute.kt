package com.example.learnretrofitokhttp.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnretrofitokhttp.data.repository.AuthRepository

@Composable
fun LoginRoute(
    authRepository: AuthRepository,
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sans remember, une nouvelle factory serait construite à chaque recomposition
    val factory = remember(authRepository) {
        authViewModelFactory(
            authRepository = authRepository
        )
    }

    val authViewModel: AuthViewModel = viewModel(
        factory = factory
    )

    val uiState by authViewModel.uiState
        .collectAsStateWithLifecycle()

    // Un changement d’écran est un effet, tandis qu'une composition doit principalement
    // décrire l’interface.
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthenticated()
        }
    }

    // LoginScreen reste purement visuel, LoginRoute le connecte au repo.
    LoginScreen(
        uiState = uiState,
        onEmailChanged = authViewModel::onEmailChanged,
        onPasswordChanged = authViewModel::onPasswordChanged,
        onLoginClick = authViewModel::login,
        modifier = modifier
    )
    // Notation équivalentes :
    //      authViewModel::onEmailChanged
    //      { newEmail -> authViewModel.onEmailChanged(newEmail) }
}

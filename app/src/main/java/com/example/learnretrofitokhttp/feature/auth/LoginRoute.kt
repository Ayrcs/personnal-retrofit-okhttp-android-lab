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
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState
        .collectAsStateWithLifecycle()

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

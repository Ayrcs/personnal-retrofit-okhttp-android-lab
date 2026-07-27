package com.example.learnretrofitokhttp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnretrofitokhttp.data.repository.AuthRepository
import com.example.learnretrofitokhttp.data.repository.TestsRepository
import com.example.learnretrofitokhttp.feature.auth.AuthViewModel
import com.example.learnretrofitokhttp.feature.auth.LoginRoute
import com.example.learnretrofitokhttp.feature.auth.authViewModelFactory
import com.example.learnretrofitokhttp.feature.tests.TestsRoute


@Composable
fun AppNavigation(
    authRepository: AuthRepository,
    testsRepository: TestsRepository
) {
    val authFactory = remember(authRepository) {
        authViewModelFactory(
            authRepository = authRepository
        )
    }

    val authViewModel: AuthViewModel = viewModel(
        factory = authFactory
    )

    val authUiState by authViewModel.uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when {
            authUiState.isInitializing -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            authUiState.isAuthenticated -> {
                TestsRoute(
                    testsRepository = testsRepository,
                    onLogout = authViewModel::logout,
                    onSessionExpired = authViewModel::onSessionExpired,
                    isLoggingOut = authUiState.isLoading,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                LoginRoute(
                    authViewModel = authViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

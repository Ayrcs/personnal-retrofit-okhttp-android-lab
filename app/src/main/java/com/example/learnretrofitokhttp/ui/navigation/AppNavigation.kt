package com.example.learnretrofitokhttp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnretrofitokhttp.R
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
    // mutableStateOf() :   Quand .value change, le composable se recharge.
    //                      On utilise alors .value pour le lire et pour l'écrire.
    //
    // remember() :         À la recomposition, les états sont perdus.
    //                      remeber Permet de se souvenir de l'état.
    //
    // by :     est une fonctionnalité Kotlin appelée délégation de propriété.
    //          permet d'éviter l'usage de .value
    //          appel les getter / setter du type

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
        if (authUiState.isAuthenticated) {
            TestsRoute(
                testsRepository = testsRepository,
                onLogout = authViewModel::logout,
                isLoggingOut = authUiState.isLoading,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LoginRoute(
                authViewModel = authViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
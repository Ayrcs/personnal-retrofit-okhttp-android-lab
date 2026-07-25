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
import com.example.learnretrofitokhttp.R
import com.example.learnretrofitokhttp.data.repository.AuthRepository
import com.example.learnretrofitokhttp.feature.auth.LoginRoute


@Composable
fun AppNavigation(
    authRepository: AuthRepository
) {
    var isAuthenticated by remember {
        mutableStateOf(
            authRepository.isAuthenticated()
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (isAuthenticated) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.login_success)
                )
            }
        } else {
            LoginRoute(
                authRepository = authRepository,
                onAuthenticated = {
                    isAuthenticated = true
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
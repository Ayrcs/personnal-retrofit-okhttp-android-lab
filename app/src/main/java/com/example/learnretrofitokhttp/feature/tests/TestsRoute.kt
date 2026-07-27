package com.example.learnretrofitokhttp.feature.tests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnretrofitokhttp.data.repository.TestsRepository

@Composable
fun TestsRoute(
    testsRepository: TestsRepository,
    modifier: Modifier = Modifier
) {
    val factory = remember(testsRepository) {
        testsViewModelFactory(
            testsRepository = testsRepository
        )
    }

    val testsViewModel: TestsViewModel = viewModel(
        factory = factory
    )

    val uiState by testsViewModel.uiState
        .collectAsStateWithLifecycle()

    TestsScreen(
        uiState = uiState,
        onRetry = testsViewModel::loadTests,
        modifier = modifier
    )
}
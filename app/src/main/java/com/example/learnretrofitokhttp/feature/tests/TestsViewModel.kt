package com.example.learnretrofitokhttp.feature.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnretrofitokhttp.data.repository.TestsRepository
import com.example.learnretrofitokhttp.data.repository.TestsResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestsViewModel(
    private val testsRepository: TestsRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TestsUiState>(
            TestsUiState.Loading
        )

    val uiState: StateFlow<TestsUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadTests()
    }

    fun loadTests() {
        if (loadingJob?.isActive == true) {
            return
        }

        _uiState.value = TestsUiState.Loading

        loadingJob = viewModelScope.launch {
            _uiState.value = when (
                val result = testsRepository.getTests()
            ) {
                // Est-ce que result est une instance de la classe Success ?
                is TestsResult.Success -> {
                    TestsUiState.Content(
                        tests = result.tests
                    )
                }

                TestsResult.Unauthorized -> {
                    TestsUiState.Error(
                        reason = TestsError.SESSION_EXPIRED
                    )
                }

                TestsResult.Forbidden -> {
                    TestsUiState.Error(
                        reason = TestsError.ACCESS_DENIED
                    )
                }

                TestsResult.NetworkError -> {
                    TestsUiState.Error(
                        reason = TestsError.NETWORK
                    )
                }

                TestsResult.ServerError -> {
                    TestsUiState.Error(
                        reason = TestsError.SERVER
                    )
                }
            }
        }


    }

}
package com.example.learnretrofitokhttp.feature.tests

import com.example.learnretrofitokhttp.data.remote.dto.TestDto

// Comme une énumération sauf qu'elle transporte des données avec.
// sealed : représente une liste fermée de possibilités ;
//          permet au compilateur de vérifier qu’on ne manque aucun cas.

sealed class TestsUiState {
    data object Loading : TestsUiState()

    data class Content(
        val tests: List<TestDto>
    ) : TestsUiState()

    data class Error(
        val reason: TestsError
    ) : TestsUiState()
}

enum class TestsError {
    SESSION_EXPIRED,
    ACCESS_DENIED,
    NETWORK,
    SERVER
}
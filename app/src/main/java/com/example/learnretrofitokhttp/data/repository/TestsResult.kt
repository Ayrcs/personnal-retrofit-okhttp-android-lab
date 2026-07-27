package com.example.learnretrofitokhttp.data.repository

import com.example.learnretrofitokhttp.data.remote.dto.TestDto

// Comme une énumération sauf qu'elle transporte des données avec.
// sealed : représente une liste fermée de possibilités ;
//          permet au compilateur de vérifier qu’on ne manque aucun cas.

sealed class TestsResult {
    // Equivalent d'un enum Success avec des données.
    data class Success(
        val tests: List<TestDto>
    ): TestsResult()

    // Equivalents d'enum d'echec sans données.
    data object Unauthorized: TestsResult()
    data object Forbidden : TestsResult()
    data object NetworkError : TestsResult()
    data object ServerError : TestsResult()
}
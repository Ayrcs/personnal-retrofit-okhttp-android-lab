package com.example.learnretrofitokhttp.data.repository

import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.dto.TestDto

class TestsRepository(
    private val api: DirectusApi
) {

    // ViewModel
    //      ↓ demande des tests
    // TestsRepository
    //      ↓ décide comment les obtenir
    // Retrofit, Room, cache, fichier…

    suspend fun getTests(): List<TestDto> {
        return api.getTests().data
    }
}
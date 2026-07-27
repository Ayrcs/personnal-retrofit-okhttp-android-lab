package com.example.learnretrofitokhttp.data.repository

import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import retrofit2.HttpException
import java.io.IOException

class TestsRepository(
    private val api: DirectusApi
) {

    // ViewModel
    //      ↓ demande des tests
    // TestsRepository
    //      ↓ décide comment les obtenir
    // Retrofit, Room, cache, fichier…

    suspend fun getTests(): TestsResult {
        // On transforme
        return try {
            val tests = api.getTests().data

            TestsResult.Success(
                tests = tests
            )

        } catch (exception: HttpException) {
            when (exception.code()) {
                401 -> TestsResult.Unauthorized
                403 -> TestsResult.Forbidden
                else -> TestsResult.ServerError
            }

        } catch (exception: IOException) {
            TestsResult.NetworkError
        }
    }
}
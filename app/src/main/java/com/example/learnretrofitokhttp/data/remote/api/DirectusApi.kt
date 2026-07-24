package com.example.learnretrofitokhttp.data.remote.api

import com.example.learnretrofitokhttp.data.remote.dto.AuthTokensDto
import com.example.learnretrofitokhttp.data.remote.dto.DirectusResponse
import com.example.learnretrofitokhttp.data.remote.dto.LoginRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.LogoutRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.RefreshRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.TestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DirectusApi {

    @POST("auth/login")
    // suspend signifie que la requête peut prendre du temps sans bloquer l’interface graphique.
    // Elle sera lancée plus tard dans une coroutine du ViewModel.
    suspend fun login(
        // @Body demande à Gson de transformer l’objet Kotlin en JSON et de l’envoyer dans le
        // corps HTTP.
        @Body request: LoginRequestDto
    ): DirectusResponse<AuthTokensDto>

    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequestDto
    )

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequestDto
    ): DirectusResponse<AuthTokensDto>

    @GET("items/tests")
    suspend fun getTests(
        @Query("fields") fields: String = TEST_FIELDS,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "-date_created"
    ): DirectusResponse<List<TestDto>>

    companion object {
        private const val TEST_FIELDS =
            "id,status,description,date_created," +
            "smartphone_id.id," +
            "smartphone_id.model_id.id," +
            "smartphone_id.model_id.manufacturer," +
            "smartphone_id.model_id.market_name," +
            "battery_id.id," +
            "battery_id.description," +
            "battery_id.design_capacity," +
            "protocol_id.id," +
            "protocol_id.description," +
            "identifications.id," +
            "identifications.algorithm," +
            "identifications.estimated_capacity"
    }

}
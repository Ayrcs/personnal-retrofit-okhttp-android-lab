package com.example.learnretrofitokhttp.data.remote

import com.example.learnretrofitokhttp.BuildConfig
import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.auth.AuthInterceptor
import com.example.learnretrofitokhttp.data.remote.auth.TokenStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DirectusNetworkClient {
    val tokenStore = TokenStore()

    private val authInterceptor = AuthInterceptor(
        tokenStore = tokenStore
    )

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        redactHeader("Authorization")

        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.HEADERS
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: DirectusApi = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DirectusApi::class.java)

    companion object {
        const val BASE_URL = "https://app.odhyssee.fr/"
    }

}
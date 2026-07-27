package com.example.learnretrofitokhttp.data.remote

import com.example.learnretrofitokhttp.BuildConfig
import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.auth.AuthInterceptor
import com.example.learnretrofitokhttp.data.remote.auth.TokenAuthenticator
import com.example.learnretrofitokhttp.data.remote.auth.TokenStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DirectusNetworkClient(
    private val tokenStore: TokenStore
) {

    private val authInterceptor = AuthInterceptor(tokenStore)

    /*
     * Ce client sert uniquement à appeler /auth/refresh.
     * Il ne possède ni AuthInterceptor ni TokenAuthenticator.
     */
    private val refreshOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(createLoggingInterceptor())
        .build()

    private val refreshApi = createApi(refreshOkHttpClient)

    private val tokenAuthenticator = TokenAuthenticator(
        tokenStore = tokenStore,
        refreshApi = refreshApi
    )

    /*
     * Client principal utilisé pour les appels authentifiés,
     * notamment /items/tests.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(createLoggingInterceptor())
        .authenticator(tokenAuthenticator)
        .build()

    val api: DirectusApi = createApi(okHttpClient)

    private fun createApi(client: OkHttpClient): DirectusApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectusApi::class.java)
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }

            redactHeader("Authorization")
        }
    }

    private companion object {
        const val BASE_URL = "https://app.odhyssee.fr/"
    }
}

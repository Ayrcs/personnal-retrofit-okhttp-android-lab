package com.example.learnretrofitokhttp.data.remote.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {

    // Authentifie les requêtes sauf si ce sont des requetes de login (/auth/)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenStore.getAccessToken()

        val isAuthenticationRequest =
            originalRequest.url.encodedPath.startsWith("/auth/")

        if (accessToken == null || isAuthenticationRequest) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest
            .newBuilder()
            .header(
                name = "Authorization",
                value = "Bearer $accessToken"
            )
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
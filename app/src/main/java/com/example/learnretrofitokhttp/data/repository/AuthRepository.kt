package com.example.learnretrofitokhttp.data.repository

import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.auth.TokenStore
import com.example.learnretrofitokhttp.data.remote.dto.AuthTokensDto
import com.example.learnretrofitokhttp.data.remote.dto.LoginRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.LogoutRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.RefreshRequestDto

class AuthRepository(
    private val api: DirectusApi,
    private val tokenStore: TokenStore
) {

    // email + password
    //      ↓
    // LoginRequestDto
    //      ↓
    // api.login()
    //      ↓
    // DirectusResponse<AuthTokensDto>
    //      ↓
    // response.data
    //      ↓
    // TokenStore.save()
    suspend fun login(
        email: String,
        password: String
    ) {
        val response = api.login(
            request = LoginRequestDto(
                email = email.trim(),
                password = password
            )
        )

        saveTokens(response.data)
    }

    suspend fun refreshSession() {
        val refreshToken = checkNotNull(
            tokenStore.getRefreshToken()
        ) {
            "No refresh token is available"
        }

        val response = api.refresh(
            request = RefreshRequestDto(
                refreshToken = refreshToken
            )
        )

        saveTokens(response.data)
    }

    suspend fun logout() {
        val refreshToken = tokenStore.getRefreshToken()

        try {
            if (refreshToken != null) {
                api.logout(
                    request = LogoutRequestDto(
                        refreshToken = refreshToken
                    )
                )
            }
        } finally {
            tokenStore.clear()
        }
    }

    fun isAuthenticated(): Boolean {
        return tokenStore.getAccessToken() != null
    }

    private fun saveTokens(tokens: AuthTokensDto) {
        tokenStore.save(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )
    }
}
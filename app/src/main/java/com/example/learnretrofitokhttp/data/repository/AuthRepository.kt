package com.example.learnretrofitokhttp.data.repository

import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.auth.TokenStore
import com.example.learnretrofitokhttp.data.remote.dto.AuthTokensDto
import com.example.learnretrofitokhttp.data.remote.dto.LoginRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.LogoutRequestDto
import com.example.learnretrofitokhttp.data.remote.dto.RefreshRequestDto
import java.io.IOException
import retrofit2.HttpException

class AuthRepository(
    private val api: DirectusApi,
    private val tokenStore: TokenStore
) {
    suspend fun restoreSession(): Boolean {
        return try {
            tokenStore.restore()
            isAuthenticated()
        } catch (_: IOException) {
            false
        }
    }

    suspend fun clearLocalSession() {
        tokenStore.clear()
    }

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
    ): LoginResult {
        return try {
            val response = api.login(
                request = LoginRequestDto(
                    email = email.trim(),
                    password = password
                )
            )

            saveTokens(response.data)
            LoginResult.SUCCESS

        } catch (exception: HttpException) {
            if (exception.code() in setOf(400, 401)) {
                LoginResult.INVALID_CREDENTIALS
            } else {
                LoginResult.SERVER_ERROR
            }

        } catch (exception: IOException) {
            LoginResult.NETWORK_ERROR
        }
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
        } catch (_: IOException) {
            // Même sans réseau, l'utilisateur doit pouvoir
            // se déconnecter localement.
        } catch (_: HttpException) {
            // Même si Directus refuse la requête,
            // les tokens locaux seront supprimés.
        } finally {
            tokenStore.clear()
        }
    }

    fun isAuthenticated(): Boolean {
        return tokenStore.getAccessToken() != null &&
            tokenStore.getRefreshToken() != null
    }

    private suspend fun saveTokens(tokens: AuthTokensDto) {
        tokenStore.save(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )
    }
}

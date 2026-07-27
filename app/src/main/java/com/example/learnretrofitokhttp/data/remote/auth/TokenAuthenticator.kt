package com.example.learnretrofitokhttp.data.remote.auth

import com.example.learnretrofitokhttp.data.remote.api.DirectusApi
import com.example.learnretrofitokhttp.data.remote.dto.RefreshRequestDto
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import kotlinx.coroutines.runBlocking

class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val refreshApi: DirectusApi
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        // L'Authenticator ne doit pas essayer de renouveler un token
        // si c'est déjà une requête d'authentification qui a échoué.
        if (response.request.url.encodedPath.startsWith("/auth/")) {
            return null
        }

        // Empêche une boucle infinie :
        // requête → 401 → refresh → nouvelle requête → 401 → etc.
        if (responseCount(response) >= 2) {
            return null
        }

        val failedAccessToken = response.request
            .header("Authorization")
            ?.removePrefix("Bearer ")

        return synchronized(this) {
            refreshAndCreateRequest(
                response = response,
                failedAccessToken = failedAccessToken
            )
        }
    }

    private fun refreshAndCreateRequest(
        response: Response,
        failedAccessToken: String?
    ): Request? {
        val latestAccessToken = tokenStore.getAccessToken()

        /*
         * Pendant que cette requête attendait, une autre requête
         * a peut-être déjà renouvelé le token.
         */
        if (
            latestAccessToken != null &&
            latestAccessToken != failedAccessToken
        ) {
            return response.request.withAccessToken(latestAccessToken)
        }

        val refreshToken = tokenStore.getRefreshToken()
            ?: return null

        val refreshResponse = try {
            refreshApi.refreshBlocking(
                RefreshRequestDto(refreshToken = refreshToken)
            ).execute()
        } catch (_: IOException) {
            // Pas de réseau : on abandonne le renouvellement.
            return null
        }

        if (!refreshResponse.isSuccessful) {
            refreshResponse.errorBody()?.close()

            /*
             * Un 401 ou un 403 signifie généralement que le refresh
             * token n'est plus accepté.
             */
            if (
                refreshResponse.code() == 401 ||
                refreshResponse.code() == 403
            ) {
                runBlocking {
                    tokenStore.clear()
                }
            }

            return null
        }

        val newTokens = refreshResponse.body()?.data
            ?: return null

        runBlocking {
            tokenStore.save(
                accessToken = newTokens.accessToken,
                refreshToken = newTokens.refreshToken
            )
        }

        return response.request.withAccessToken(newTokens.accessToken)
    }

    private fun Request.withAccessToken(accessToken: String): Request {
        return newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var previousResponse = response.priorResponse

        while (previousResponse != null) {
            count++
            previousResponse = previousResponse.priorResponse
        }

        return count
    }
}

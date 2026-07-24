package com.example.learnretrofitokhttp.data.remote.auth

class TokenStore {
    // @Volatile garantit que les différents threads utilisés par OkHttp voient la valeur
    // la plus récente.
    @Volatile
    private var tokens: Tokens? = null

    fun save(
        accessToken: String,
        refreshToken: String
    ) {
        tokens = Tokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun getAccessToken(): String? {
        return tokens?.accessToken
    }

    fun getRefreshToken(): String? {
        return tokens?.refreshToken
    }

    fun clear() {
        tokens = null
    }

    private data class Tokens(
        val accessToken: String,
        val refreshToken: String
    )
}
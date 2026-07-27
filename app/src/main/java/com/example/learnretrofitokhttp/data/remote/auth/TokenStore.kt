package com.example.learnretrofitokhttp.data.remote.auth

class TokenStore(
    private val encryptedTokenStorage: EncryptedTokenStorage
) {
    // @Volatile garantit que les différents threads utilisés par OkHttp voient la valeur
    // la plus récente.
    @Volatile
    private var tokens: Tokens? = null

    /*
     * Charge les tokens persistés dans le cache mémoire.
     * Cette fonction sera appelée au démarrage à l'étape suivante.
     */
    suspend fun restore() {
        val storedTokens = encryptedTokenStorage.read()

        tokens = storedTokens?.let {
            Tokens(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
        }
    }

    suspend fun save(
        accessToken: String,
        refreshToken: String
    ) {
        encryptedTokenStorage.save(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

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

    suspend fun clear() {
        // OkHttp cesse immédiatement d'utiliser les tokens, avant l'accès au disque.
        tokens = null
        encryptedTokenStorage.clear()
    }

    private data class Tokens(
        val accessToken: String,
        val refreshToken: String
    )
}

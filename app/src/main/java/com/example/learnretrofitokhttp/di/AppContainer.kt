package com.example.learnretrofitokhttp.di

import android.content.Context
import com.example.learnretrofitokhttp.data.remote.DirectusNetworkClient
import com.example.learnretrofitokhttp.data.remote.auth.EncryptedTokenStorage
import com.example.learnretrofitokhttp.data.remote.auth.TokenCipher
import com.example.learnretrofitokhttp.data.remote.auth.TokenStore
import com.example.learnretrofitokhttp.data.repository.AuthRepository
import com.example.learnretrofitokhttp.data.repository.TestsRepository

class AppContainer(
    context: Context
) {
    private val tokenCipher = TokenCipher()

    private val encryptedTokenStorage = EncryptedTokenStorage(
        context = context,
        tokenCipher = tokenCipher
    )

    val tokenStore = TokenStore(
        encryptedTokenStorage = encryptedTokenStorage
    )

    // Privée car le reste de l’application ne doit pas contourner les repositories avec
    // container.networkClient.api.getTests()
    private val networkClient = DirectusNetworkClient(
        tokenStore = tokenStore
    )

    // C’est une injection par constructeur. Les repositories ne construisent pas eux-mêmes
    // leurs dépendances.
    val authRepository = AuthRepository(
        api = networkClient.api,
        tokenStore = tokenStore
    )

    val testsRepository = TestsRepository(
        api = networkClient.api
    )
}

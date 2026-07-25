package com.example.learnretrofitokhttp.di

import com.example.learnretrofitokhttp.data.remote.DirectusNetworkClient
import com.example.learnretrofitokhttp.data.repository.AuthRepository
import com.example.learnretrofitokhttp.data.repository.TestsRepository

class AppContainer {
    // Privée car le reste de l’application ne doit pas contourner les repositories avec
    // container.networkClient.api.getTests()
    private val networkClient = DirectusNetworkClient()

    // C’est une injection par constructeur. Les repositories ne construisent pas eux-mêmes
    // leurs dépendances.
    val authRepository = AuthRepository(
        api = networkClient.api,
        tokenStore = networkClient.tokenStore
    )

    val testsRepository = TestsRepository(
        api = networkClient.api
    )
}

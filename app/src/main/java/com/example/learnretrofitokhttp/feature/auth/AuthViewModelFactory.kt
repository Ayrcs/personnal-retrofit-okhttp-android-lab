package com.example.learnretrofitokhttp.feature.auth

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learnretrofitokhttp.data.repository.AuthRepository

// Une factory permet de créer le viewModel et d'appeler l'instance existante à chaque appel.
// Puisque le AuthViewModel nécessite qu'on lui pass en argument un repository, on est obligé
// d'utiliser une factory, sinon on ne pourrait pas le renseigner.

fun authViewModelFactory(
    authRepository: AuthRepository
): ViewModelProvider.Factory {
    return viewModelFactory {
        initializer {
            AuthViewModel(
                authRepository = authRepository
            )
        }
    }
}
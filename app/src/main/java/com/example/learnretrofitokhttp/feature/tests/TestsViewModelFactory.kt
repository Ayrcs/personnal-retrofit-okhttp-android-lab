package com.example.learnretrofitokhttp.feature.tests

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learnretrofitokhttp.data.repository.TestsRepository

fun testsViewModelFactory(
    testsRepository: TestsRepository
): ViewModelProvider.Factory {
    return viewModelFactory {
        initializer {
            TestsViewModel(
                testsRepository = testsRepository
            )
        }
    }
}
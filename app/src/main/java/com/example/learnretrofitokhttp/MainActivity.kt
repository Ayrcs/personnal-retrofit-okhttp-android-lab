package com.example.learnretrofitokhttp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learnretrofitokhttp.ui.navigation.AppNavigation
import com.example.learnretrofitokhttp.ui.theme.LearnRetrofitOkHttpTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // On récupère l'objet application en le castant par LearnRetrofitOkHttpApplication
        val application = application as LearnRetrofitOkHttpApplication
        val authRepository = application.container.authRepository
        val testsRepository = application.container.testsRepository

        // Permet à la fenêtre de l’application d’occuper toute la surface :
        // derrière la barre supérieure d'état + derrière la zone inférieure de navigation
        enableEdgeToEdge()

        setContent {
            LearnRetrofitOkHttpTheme {
                AppNavigation(
                    authRepository = authRepository,
                    testsRepository = testsRepository
                )
            }
        }
    }
}
package com.example.learnretrofitokhttp

import android.app.Application
import com.example.learnretrofitokhttp.di.AppContainer

// Représente l’application au niveau du processus Android, et non un écran.
//
// Processus de l’application
// └── Application
//    ├── MainActivity
//    ├── éventuellement d’autres Activity
//    └── services éventuels

// Android crée normalement une seule instance de Application par processus.
// Une Activity représente un écran et peut être détruite puis recréée, par exemple lors d’une
// rotation. La classe Application fournit également un contexte global.

class LearnRetrofitOkHttpApplication : Application() {

    // Sans by lazy, le conteneur serait construit immédiatement au démarrage.
    // Les deux approches fonctionneraient, mais l’initialisation différée évite de construire le
    // réseau tant qu’il n’est pas utilisé.

    val container: AppContainer by lazy {
        AppContainer(
            context = applicationContext
        )
    }
}

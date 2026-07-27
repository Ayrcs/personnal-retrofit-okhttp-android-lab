package com.example.learnretrofitokhttp.data.remote.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import java.security.GeneralSecurityException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

/*
 * Cette propriété doit être déclarée une seule fois,
 * au niveau supérieur du fichier.
 */
private val Context.tokenDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "auth_tokens"
)

class EncryptedTokenStorage(
    context: Context,
    private val tokenCipher: TokenCipher
) {
    /*
     * applicationContext évite de conserver une Activity
     * et donc d'empêcher sa destruction.
     */
    private val dataStore =
        context.applicationContext.tokenDataStore

    suspend fun save(
        accessToken: String,
        refreshToken: String
    ) {
        /*
         * Le chiffrement est effectué avant l'écriture.
         * DataStore ne reçoit jamais les tokens originaux.
         */
        val encryptedAccessToken =
            tokenCipher.encrypt(accessToken)

        val encryptedRefreshToken =
            tokenCipher.encrypt(refreshToken)

        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] =
                encryptedAccessToken

            preferences[REFRESH_TOKEN_KEY] =
                encryptedRefreshToken
        }
    }

    suspend fun read(): StoredTokens? {
        val preferences = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .first()

        val encryptedAccessToken =
            preferences[ACCESS_TOKEN_KEY]

        val encryptedRefreshToken =
            preferences[REFRESH_TOKEN_KEY]

        if (
            encryptedAccessToken == null ||
            encryptedRefreshToken == null
        ) {
            return null
        }

        return try {
            StoredTokens(
                accessToken = tokenCipher.decrypt(
                    encryptedAccessToken
                ),
                refreshToken = tokenCipher.decrypt(
                    encryptedRefreshToken
                )
            )
        } catch (_: GeneralSecurityException) {
            /*
             * La clé peut avoir disparu ou les données peuvent
             * avoir été altérées. Elles deviennent inutilisables.
             */
            clear()
            null
        } catch (_: IllegalArgumentException) {
            /*
             * Les données enregistrées ne respectent pas
             * le format attendu.
             */
            clear()
            null
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    data class StoredTokens(
        val accessToken: String,
        val refreshToken: String
    )

    private companion object {
        val ACCESS_TOKEN_KEY =
            stringPreferencesKey("access_token_encrypted")

        val REFRESH_TOKEN_KEY =
            stringPreferencesKey("refresh_token_encrypted")
    }
}
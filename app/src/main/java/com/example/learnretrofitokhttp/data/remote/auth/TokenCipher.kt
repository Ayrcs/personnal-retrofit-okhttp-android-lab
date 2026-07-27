package com.example.learnretrofitokhttp.data.remote.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.ByteBuffer
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class TokenCipher {

    private val keyStore = KeyStore
        .getInstance(KEYSTORE_PROVIDER)
        .apply {
            load(null)
        }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey()
        )

        val encryptedBytes = cipher.doFinal(
            value.toByteArray(Charsets.UTF_8)
        )

        val initializationVector = cipher.iv

        /*
         * Nous réunissons dans un seul tableau :
         *
         * taille de l'IV + IV + données chiffrées
         */
        val result = ByteBuffer.allocate(
            Int.SIZE_BYTES +
                    initializationVector.size +
                    encryptedBytes.size
        )
            .putInt(initializationVector.size)
            .put(initializationVector)
            .put(encryptedBytes)
            .array()

        return Base64.encodeToString(
            result,
            Base64.NO_WRAP
        )
    }

    fun decrypt(encryptedValue: String): String {
        val storedBytes = Base64.decode(
            encryptedValue,
            Base64.NO_WRAP
        )

        require(storedBytes.size > Int.SIZE_BYTES) {
            "Invalid encrypted token"
        }

        val buffer = ByteBuffer.wrap(storedBytes)
        val initializationVectorSize = buffer.int

        require(
            initializationVectorSize in MIN_IV_SIZE..MAX_IV_SIZE &&
                    buffer.remaining() > initializationVectorSize
        ) {
            "Invalid initialization vector"
        }

        val initializationVector =
            ByteArray(initializationVectorSize)

        buffer.get(initializationVector)

        val encryptedBytes =
            ByteArray(buffer.remaining())

        buffer.get(encryptedBytes)

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(
                AUTHENTICATION_TAG_SIZE_BITS,
                initializationVector
            )
        )

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return decryptedBytes.toString(Charsets.UTF_8)
    }

    @Synchronized
    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = keyStore.getKey(
            KEY_ALIAS,
            null
        ) as? SecretKey

        if (existingKey != null) {
            return existingKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_PROVIDER
        )

        val keyParameters = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(
                KeyProperties.BLOCK_MODE_GCM
            )
            .setEncryptionPaddings(
                KeyProperties.ENCRYPTION_PADDING_NONE
            )
            .setKeySize(KEY_SIZE_BITS)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(keyParameters)

        return keyGenerator.generateKey()
    }

    private companion object {
        const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        const val KEY_ALIAS = "directus_tokens_key"

        const val TRANSFORMATION = "AES/GCM/NoPadding"

        const val KEY_SIZE_BITS = 256
        const val AUTHENTICATION_TAG_SIZE_BITS = 128

        const val MIN_IV_SIZE = 12
        const val MAX_IV_SIZE = 16
    }
}
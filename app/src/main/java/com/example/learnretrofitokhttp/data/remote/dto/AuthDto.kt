package com.example.learnretrofitokhttp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Ces classes représentent le format du serveur.
// Elles ne doivent pas être utilisées pour stocker directement des informations de formulaire
// dans l’interface.

data class LoginRequestDto(
    val email: String,
    val password: String,
    val mode: String = "json"
)

data class RefreshRequestDto(
    @SerializedName("refresh_token")
    val refreshToken: String,

    val mode: String = "json"
)

data class AuthTokensDto(
    @SerializedName("access_token")
    val accessToken: String,

    val expires: Long,

    @SerializedName("refresh_token")
    val refreshToken: String
)
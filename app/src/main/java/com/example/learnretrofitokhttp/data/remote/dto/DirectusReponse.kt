package com.example.learnretrofitokhttp.data.remote.dto

//Le contenu de data peut changer :
//  DirectusResponse<List<TestDto>>
//  DirectusResponse<TestDto>
//  DirectusResponse<LoginDataDto>
//T est un type générique. Il signifie : « le type exact sera indiqué au moment d’utiliser cette classe ».
data class DirectusResponse<T>(
    val data: T
)
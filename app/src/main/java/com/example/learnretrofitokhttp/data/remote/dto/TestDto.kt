package com.example.learnretrofitokhttp.data.remote.dto

import com.google.gson.annotations.SerializedName

// @SerializedName("date_created")
// Permet de passer de snake_case à camelCase

data class TestDto(
    val id: String,
    val status: String?,
    val description: String?,

    @SerializedName("date_created")
    val dateCreated: String?,

    @SerializedName("smartphone_id")
    val smartphone: SmartphoneDto?,

    @SerializedName("battery_id")
    val battery: BatteryDto?,

    @SerializedName("protocol_id")
    val protocol: ProtocolDto?,

    val identifications: List<IdentificationDto>?
)

data class SmartphoneDto(
    val id: String,

    @SerializedName("model_id")
    val model: SmartphoneModelDto?
)

data class SmartphoneModelDto(
    val id: String,
    val manufacturer: String?,

    @SerializedName("market_name")
    val marketName: String?
)

data class BatteryDto(
    val id: String,
    val description: String?,

    @SerializedName("design_capacity")
    val designCapacity: Double?
)

data class ProtocolDto(
    val id: String,
    val description: String?
)

data class IdentificationDto(
    val id: String,
    val algorithm: String?,

    @SerializedName("estimated_capacity")
    val estimatedCapacity: Double?
)
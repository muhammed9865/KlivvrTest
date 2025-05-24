package com.salman.klivvrandroidchallenge.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class CityEntity(
    @SerialName("coord")
    val coordinates: Coordinates,
    val country: String,
    @SerialName("_id")
    val id: Int,
    val name: String
) {
    @Keep
    @Serializable
    data class Coordinates(
        val lat: Double,
        val lon: Double
    )
}
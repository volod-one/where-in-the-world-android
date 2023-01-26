package com.example.whereintheworld.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val name: String,
    val independent: Boolean,
    val capital: List<String>,
    val region: String,
    @SerialName ("subregion") val subRegion: String,
    val language: List<String>,
    val borders: List<String>,
    val flag: String
)

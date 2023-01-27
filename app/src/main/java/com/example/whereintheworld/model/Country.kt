package com.example.whereintheworld.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val name: Name? = null,
    val tld: List<String>? = listOf(),
    val cca2: String? = null,
    val ccn3: String? = null,
    val cca3: String? = null,
    val cioc: String? = null,
    val independent: Boolean? = null,
    val status: String? = null,
    val unMember: Boolean? = null,
    val currencies: Map<String, Map<String, String>>? = null,
    val idd: IDD? = null,
    val capital: List<String>? = null,
    val altSpellings: List<String>? = null,
    val region: String? = null,
    val subregion: String? = null,
    val languages: Map<String, String>? = null,
    val translations: Map<String, Map<String, String>>? = null,
    val latlng: List<Double>? = null,
    val landlocked: Boolean? = null,
    val borders: List<String>? = null,
    val area: Double? = null,
    val demonyms: Map<String, Map<String, String>>? = null,
    val flag: String? = null,
    val maps: Map<String, String>? = null,
    val population: Int? = null,
    val gini: Map<String, Double>? = null,
    val fifa: String? = null,
    val car: Car? = null,
    val timezones: List<String>? = null,
    val continents: List<String>? = null,
    val flags: Map<String, String>? = null,
    val coatOfArms: Map<String, String>? = null,
    val startOfWeek: String? = null,
    val capitalInfo: Map<String, List<Double>>? = null,
    val postalCode: PostalCode? = null,
)


@Serializable
data class PostalCode(
    val format: String? = null,
    val regex: String? = null,
)

@Serializable
data class Name(
    val common: String? = null,
    val official: String? = null,
    val nativeName: Map<String, Map<String, String>>? = mapOf(),
)


@Serializable
data class IDD(
    val root: String? = null,
    val suffixes: List<String>? = listOf()
)

@Serializable
data class Car(
    val signs: List<String>? = listOf(),
    val side: String? = null,
)
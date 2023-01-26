package com.example.whereintheworld.data

import com.example.whereintheworld.model.Country
import com.example.whereintheworld.network.CountriesApiService

// Defining what kind of API requests will handle this repository
// and what kind of data it will handle
interface CountriesRepository {
    suspend fun getAllCountries(): List<Country>
}

// Implementing API request handling
// taking service as parameter and calling their methods
class DefaultRepository(
    private val countriesApiService: CountriesApiService
) : CountriesRepository {
    override suspend fun getAllCountries() = countriesApiService.getAllCountries()
}
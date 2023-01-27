package com.example.whereintheworld.network

import com.example.whereintheworld.model.Country
import retrofit2.http.GET

// Defining endpoints and required data type
interface CountriesApiService {
    @GET("all")
    suspend fun getAllCountries(): List<Country>
}

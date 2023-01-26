package com.example.whereintheworld.data

import com.example.whereintheworld.network.CountriesApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

// Defining required repositories
interface AppContainer {
    val countriesRepository: CountriesRepository
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://restcountries.com/v3.1/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL).build()

    private val retrofitService: CountriesApiService by lazy {
        retrofit.create(CountriesApiService::class.java)
    }

    override val countriesRepository: CountriesRepository by lazy {
        DefaultRepository(retrofitService)
    }
}
package com.example.whereintheworld.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.whereintheworld.CountriesApplication
import com.example.whereintheworld.data.CountriesRepository
import com.example.whereintheworld.model.Country
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CountriesUiState {
    data class Success(
        val rawCountries: List<Country>,
        val countries: List<Country> = rawCountries,
        val filterInput: String = "",
    ) : CountriesUiState

    object Loading : CountriesUiState
    object Error : CountriesUiState
}

class CountriesViewModel(private val countriesRepository: CountriesRepository) : ViewModel() {

    var countriesUiState: CountriesUiState by mutableStateOf(CountriesUiState.Loading)
        private set

    init {
        setAllCountries()
    }

    fun setAllCountries() {
        viewModelScope.launch {
            countriesUiState = CountriesUiState.Loading
            countriesUiState = try {
                CountriesUiState.Success(rawCountries = countriesRepository.getAllCountries())
            } catch (e: IOException) {
                CountriesUiState.Error
            } catch (e: HttpException) {
                CountriesUiState.Error
            }
        }
    }

    fun updateFilter(input: String) {
        val prevUiState = countriesUiState as CountriesUiState.Success
        if (countriesUiState is CountriesUiState.Success) {
            countriesUiState = CountriesUiState.Success(
                filterInput = input,
                rawCountries = prevUiState.rawCountries,
                countries = if (input.isNotBlank()) {
                    println(input)
                    prevUiState.rawCountries.filter {
                        it.name?.common?.lowercase()?.contains(input.lowercase())!!
                    }
                } else {
                    prevUiState.rawCountries
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CountriesApplication)
                val countriesRepository = application.container.countriesRepository
                CountriesViewModel(countriesRepository)
            }
        }
    }
}
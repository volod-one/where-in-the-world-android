package com.example.whereintheworld.ui

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

sealed interface AppUiState {
    data class Success(val rawCountries: List<Country>) : AppUiState
    object Loading : AppUiState
    object Error : AppUiState
}

class AppViewModel(private val countriesRepository: CountriesRepository) : ViewModel() {

    var appUiState: AppUiState by mutableStateOf(AppUiState.Loading)
        private set

    init {
        requestData()
    }

    fun requestData() {
        viewModelScope.launch {
            appUiState = AppUiState.Loading
            appUiState = try {
                AppUiState.Success(rawCountries = countriesRepository.getAllCountries())
            } catch (e: IOException) {
                AppUiState.Error
            } catch (e: HttpException) {
                AppUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CountriesApplication)
                val countriesRepository = application.container.countriesRepository
                AppViewModel(countriesRepository)
            }
        }
    }
}
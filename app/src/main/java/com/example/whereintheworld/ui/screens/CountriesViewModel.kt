package com.example.whereintheworld.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.whereintheworld.model.Country
import com.example.whereintheworld.model.CategoryFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CountriesUiState(
    val rawCountries: List<Country>,
    val countries: List<Country> = rawCountries,
    val inputFilter: String = "",
    val currentCountryStack: List<Country> = mutableListOf(),
    val categories: List<CategoryFilter>
)

class CountriesViewModel(private val rawCountries: List<Country>) : ViewModel() {
    private var categories = mutableStateListOf<CategoryFilter>()


    private val _countriesUiState =
        MutableStateFlow(CountriesUiState(rawCountries = rawCountries, categories = categories))
    val countriesUiState: StateFlow<CountriesUiState> = _countriesUiState.asStateFlow()

    init {
        setCategories()
    }

    // InApp input handler
    // When user enter some value, filter all list base on input
    fun updateInputFilter(input: String = "") {
        _countriesUiState.update { curState ->
            curState.copy(
                inputFilter = input,
                countries = if (input.isNotBlank()) {
                    rawCountries.filter {
                        it.name?.common?.lowercase()?.contains(input.lowercase())!!
                    }
                } else {
                    curState.rawCountries
                }
            )
        }
    }

    // When user selects chips, filter list base on selections
    fun selectCategoryFilter(category: CategoryFilter) {
        val newList = mutableListOf<CategoryFilter>()
        countriesUiState.value.categories.forEach {
            when (it) {
                category -> newList.add(CategoryFilter(it.title, !it.isSelected))
                else -> newList.add(it)
            }
        }
        categories.clear()
        categories.addAll(newList)
        _countriesUiState.update { curState ->
            curState.copy(
                categories = categories,
                // if category is not selected, do nothing
                countries = if (categories.none { it.isSelected }) {
                    curState.rawCountries
                } else {
                    val selectedCategories = curState.categories.filter { it.isSelected }
                    // if category selected, show only selected
                    curState.rawCountries.filter { country ->
                        selectedCategories.any { categoryFilter -> categoryFilter.title == country.region }
                    }
                }
            )
        }
    }

    // initially create dynamic categories for chips
    private fun setCategories() {
        val temp = mutableSetOf<CategoryFilter>()
        countriesUiState.value.countries.forEach {
            if (it.region != null) {
                temp.add(CategoryFilter(title = it.region))
            }
        }
        categories.addAll(temp.toList())

        _countriesUiState.update { curState ->
            curState.copy(categories = categories)
        }
    }

    // When user selects a country, put it in stack (required for navigation)
    fun selectCountry(country: Country) {
        _countriesUiState.update { curState ->
            curState.copy(
                currentCountryStack = curState.currentCountryStack.plus(country)
            )
        }
    }

    // When user go on previous page, remove item from stack (required for navigation)
    fun stepBack() {
        if (countriesUiState.value.currentCountryStack.isNotEmpty()) {
            _countriesUiState.value.currentCountryStack.dropLast(1)
        }
    }

    // When user go to home page, clear stack (required for navigation)
    fun clearCountryStack() {
        _countriesUiState.update { curState ->
            curState.copy(currentCountryStack = mutableListOf())
        }
    }
}
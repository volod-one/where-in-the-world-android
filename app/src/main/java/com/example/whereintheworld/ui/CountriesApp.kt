package com.example.whereintheworld.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whereintheworld.ui.screens.*

@Composable
fun CountriesApp() {
    val viewModel: CountriesViewModel = viewModel(factory = CountriesViewModel.Factory)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        HomeScreen(
            uiState = viewModel.countriesUiState,
            onInputChange = viewModel::updateFilter,
            onTryAgain = viewModel::setAllCountries
        )
    }
}

@Composable
fun HomeScreen(
    uiState: CountriesUiState,
    onInputChange: (String) -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is CountriesUiState.Loading -> LoadingScreen()
        is CountriesUiState.Error -> ErrorScreen(onClick = onTryAgain)
        is CountriesUiState.Success -> CountriesScreen(
            input = uiState.filterInput,
            countries = uiState.countries,
            modifier = modifier,
            onInputChange = onInputChange
        )
    }
}

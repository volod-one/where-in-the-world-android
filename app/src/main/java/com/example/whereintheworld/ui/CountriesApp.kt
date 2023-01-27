package com.example.whereintheworld.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whereintheworld.R
import com.example.whereintheworld.model.Country
import com.example.whereintheworld.ui.screens.*

enum class AppScreens {
    CountriesScreen, CountryScreen
}

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
            onTryAgain = viewModel::setAllCountries,
            onCountryClick = viewModel::selectCountry
        )
    }
}

@Composable
fun HomeScreen(
    uiState: CountriesUiState,
    onInputChange: (String) -> Unit,
    onTryAgain: () -> Unit,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is CountriesUiState.Loading -> LoadingScreen()
        is CountriesUiState.Error -> ErrorScreen(onClick = onTryAgain)
        is CountriesUiState.Success -> {
            val navController = rememberNavController()

            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                // TODO
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                                    contentDescription = null
                                )
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.h5,
                            )
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.CountriesScreen.name
                ) {

                    composable(route = AppScreens.CountriesScreen.name) {
                        CountriesScreen(
                            input = uiState.filterInput,
                            countries = uiState.countries,
                            modifier = modifier.padding(
                                bottom = innerPadding.calculateBottomPadding()
                            ),
                            countriesCounter = "${uiState.countries.size}/${uiState.rawCountries.size}",
                            onInputChange = onInputChange,
                            onCountryClick = {
                                onCountryClick(it)
                                navController.navigate(AppScreens.CountryScreen.name)
                                println(navController.backQueue.joinToString())
                            }
                        )
                    }

                    composable(route = AppScreens.CountryScreen.name) {
                        CountryScreen(country = uiState.currentCountry!!)
                    }
                }
            }
        }
    }
}

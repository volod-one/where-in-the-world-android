package com.example.whereintheworld.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whereintheworld.R
import com.example.whereintheworld.model.Country
import com.example.whereintheworld.ui.screens.*

enum class AppScreens(@StringRes val title: Int) {
    CountriesScreen(title = R.string.app_name), CountryScreen(title = R.string.country)
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

@OptIn(ExperimentalMaterial3Api::class)
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
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = AppScreens.valueOf(
                backStackEntry?.destination?.route ?: AppScreens.CountriesScreen.name
            )

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            println(currentScreen)
                            Text(
                                text = when (currentScreen) {
                                    AppScreens.CountriesScreen -> stringResource(R.string.app_name)
                                    AppScreens.CountryScreen -> stringResource(
                                        R.string.country,
                                        uiState.currentCountry?.name?.common ?: "Unknown"
                                    )
                                },
                                style = MaterialTheme.typography.h5,
                            )
                        },
                        navigationIcon = {
                            if (navController.previousBackStackEntry != null) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Go back"
                                    )
                                }
                            }
                        },
                        actions = {
                            if (navController.previousBackStackEntry != null) {
                                IconButton(onClick = {
                                    navController.popBackStack(
                                        AppScreens.CountriesScreen.name,
                                        false
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Go to start"
                                    )
                                }
                            }
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

package com.example.whereintheworld.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whereintheworld.R
import com.example.whereintheworld.ui.screens.*

enum class AppScreens(@StringRes val title: Int) {
    CountriesScreen(title = R.string.app_name), CountryScreen(title = R.string.country)
}

@Composable
fun CountriesApp() {
    val viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        when (viewModel.appUiState) {
            is AppUiState.Loading -> LoadingScreen()
            is AppUiState.Error -> ErrorScreen(onClick = { viewModel.requestData() })
            is AppUiState.Success -> {
                HomeScreen(
                    countriesViewModel = CountriesViewModel(
                        (viewModel.appUiState as AppUiState.Success).rawCountries
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    countriesViewModel: CountriesViewModel = viewModel(),
) {
    val countriesUiState by countriesViewModel.countriesUiState.collectAsState()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreens.valueOf(
        backStackEntry?.destination?.route ?: AppScreens.CountriesScreen.name
    )

    if (navController.previousBackStackEntry != null) {
        BackHandler() {
            stepBack(navController = navController, viewModel = countriesViewModel)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            AppScreens.CountriesScreen -> stringResource(R.string.app_name)
                            AppScreens.CountryScreen -> stringResource(
                                R.string.country,
                                countriesUiState.currentCountryStack.lastOrNull()?.name?.common
                                    ?: "Unknown"
                            )
                        },
                        style = MaterialTheme.typography.h5,
                    )
                },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = {
                            stepBack(navController = navController, viewModel = countriesViewModel)
                        }) {
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
                            goHome(navController = navController, viewModel = countriesViewModel)
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
                    input = countriesUiState.inputFilter,
                    countries = countriesUiState.countries,
                    categories = countriesUiState.categories,
                    countriesCounter = "${countriesUiState.countries.size}/${countriesUiState.rawCountries.size}",
                    onInputChange = countriesViewModel::updateInputFilter,
                    onCountryClick = {
                        countriesViewModel.selectCountry(it)
                        navController.navigate(AppScreens.CountryScreen.name)
                    },
                    onFilterClick = countriesViewModel::selectCategoryFilter,
                    modifier = modifier.padding(
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
            }

            composable(route = AppScreens.CountryScreen.name) {
                CountryScreen(
                    country = countriesUiState.currentCountryStack.lastOrNull()
                )
            }
        }
    }
}

private fun stepBack(navController: NavHostController, viewModel: CountriesViewModel) {
    navController.navigateUp()
    viewModel.stepBack()
}

private fun goHome(navController: NavHostController, viewModel: CountriesViewModel) {
    navController.popBackStack(AppScreens.CountriesScreen.name, false)
    viewModel.clearCountryStack()
}

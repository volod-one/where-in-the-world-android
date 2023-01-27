package com.example.whereintheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whereintheworld.model.Country
import com.example.whereintheworld.ui.screens.CountriesUiState
import com.example.whereintheworld.ui.screens.CountriesViewModel
import com.example.whereintheworld.ui.theme.WhereInTheWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhereInTheWorldTheme {
                CountriesApp()
            }
        }
    }
}

@Composable
fun CountriesApp() {
    val viewModel: CountriesViewModel =
        viewModel(factory = CountriesViewModel.Factory)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        HomeScreen(
            uiState = viewModel.countriesUiState,
            onInputChange = viewModel::updateFilter
        )
    }
}

@Composable
fun HomeScreen(
    uiState: CountriesUiState,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is CountriesUiState.Loading -> Text(text = "Loading...")
        is CountriesUiState.Error -> Text(text = "Error!!!")
        is CountriesUiState.Success -> CountriesScreen(
            input = uiState.filterInput,
            countries = uiState.countries,
            modifier = modifier,
            onInputChange = onInputChange
        )
    }
}

@Composable
fun CountriesScreen(
    input: String,
    countries: List<Country>,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(
                8.dp, 8.dp, 8.dp, paddingValues.calculateBottomPadding()
            )
        ) {
            Card(elevation = 4.dp) {
                OutlinedTextField(
                    value = input,
                    onValueChange = onInputChange,
                    placeholder = { Text(text = "Write the name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CountriesList(
                countries = countries,
                modifier = modifier
            )
        }
    }

}

@Composable
fun CountriesList(countries: List<Country>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(countries, key = { item -> item.name?.common!! }) { country ->
                CountryCard(country)
            }
        }
    }
}

@Composable
fun CountryCard(country: Country, modifier: Modifier = Modifier) {
    val countryCommonName = country.name?.common ?: "Unknown Country"
    val population = country.population
    Card(
        modifier = modifier
            .padding(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(country.flags?.get("png"))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp, max = 100.dp),

                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 8.dp)
            ) {
                Text(
                    text = countryCommonName,
                    modifier = Modifier,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = stringResource(
                        R.string.population, when (population != null) {
                            true -> String.format("%,d", population)
                            else -> "Unknown"
                        }
                    ),
                    modifier = Modifier,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}
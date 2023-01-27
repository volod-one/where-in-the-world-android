package com.example.whereintheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: CountriesViewModel =
                        viewModel(factory = CountriesViewModel.Factory)
                    CountriesApp(uiState = viewModel.countriesUiState)
                }
            }
        }
    }
}

@Composable
fun CountriesApp(uiState: CountriesUiState, modifier: Modifier = Modifier) {
    when (uiState) {
        is CountriesUiState.Loading -> Text(text = "Loading...")
        is CountriesUiState.Error -> Text(text = "Error!!!")
        is CountriesUiState.Success -> CountriesList(
            countries = uiState.countries,
            modifier = modifier
        )
    }
}

@Composable
fun CountriesList(countries: List<Country>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 8.dp)) {
                Text(
                    text = countryCommonName,
                    modifier = Modifier
                )
            }
        }
    }
}
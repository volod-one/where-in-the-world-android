package com.example.whereintheworld.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.whereintheworld.R
import com.example.whereintheworld.model.Country
import com.example.whereintheworld.model.CategoryFilter

@Composable
fun CountriesScreen(
    input: String,
    countries: List<Country>,
    countriesCounter: String,
    categories: List<CategoryFilter>,
    onInputChange: (String) -> Unit,
    onCountryClick: (Country) -> Unit,
    onFilterClick: (CategoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
    ) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                placeholder = { Text(text = stringResource(R.string.search_for_a_country)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                trailingIcon = {
                    AnimatedVisibility(visible = input.isNotBlank() || categories.any { it.isSelected }) {
                        Text(
                            text = countriesCounter,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search"
                    )
                },
            )
        }

        ChipList(
            chipCategories = categories,
            modifier = Modifier.padding(horizontal = 12.dp),
            onFilterClick = onFilterClick
        )

        CountriesList(
            countries = countries,
            modifier = modifier,
            onCountryClick = onCountryClick
        )
    }
}

@Composable
fun CountriesList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(countries, key = { item -> item.name?.common!! }) { country ->
            CountryCard(
                country,
                onClick = onCountryClick
            )
        }
    }
}

@Composable
fun CountryCard(
    country: Country,
    modifier: Modifier = Modifier,
    onClick: (Country) -> Unit
) {
    val countryCommonName = country.name?.common
    val population = country.population
    val region = country.region
    val capital = country.capital?.get(0)
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick(country) },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(country.flags?.get("svg"))
                    .decoderFactory(SvgDecoder.Factory())
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
                    .padding(12.dp, 16.dp, 12.dp, 12.dp)
            ) {
                Text(
                    text = countryCommonName ?: stringResource(R.string.unknown),
                    modifier = Modifier,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.population, when (population != null) {
                                true -> String.format("%,d", population)
                                else -> stringResource(R.string.unknown)
                            }
                        ),
                        modifier = Modifier,
                        style = MaterialTheme.typography.body2,
                    )
                    Text(
                        text = stringResource(
                            R.string.region,
                            region ?: stringResource(R.string.unknown)
                        ),
                        modifier = Modifier,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                    Text(
                        text = stringResource(
                            R.string.capital,
                            capital ?: "-"
                        ),
                        modifier = Modifier,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipList(
    modifier: Modifier = Modifier,
    chipCategories: List<CategoryFilter>,
    onFilterClick: (CategoryFilter) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chipCategories) { category ->
            FilterChip(
                selected = category.isSelected,
                leadingIcon = {
                    AnimatedVisibility(visible = category.isSelected) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected"
                        )
                    }
                },
                label = { Text(text = category.title) },
                onClick = { onFilterClick(category) }
            )
        }
    }
}

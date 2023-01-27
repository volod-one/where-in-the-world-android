package com.example.whereintheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
        is CountriesUiState.Success -> Text(text = uiState.countries.joinToString())
    }
}
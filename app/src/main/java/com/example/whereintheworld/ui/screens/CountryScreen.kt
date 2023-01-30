package com.example.whereintheworld.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.whereintheworld.model.Country

@Composable
fun CountryScreen(
    modifier: Modifier = Modifier,
    country: Country? = null
) {
    Column(modifier = modifier) {
        Text(text = country?.name?.common ?: "Not selected")
    }
}
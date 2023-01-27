package com.example.whereintheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.whereintheworld.ui.CountriesApp
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

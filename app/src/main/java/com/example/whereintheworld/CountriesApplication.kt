package com.example.whereintheworld

import android.app.Application
import com.example.whereintheworld.data.AppContainer
import com.example.whereintheworld.data.DefaultAppContainer

class CountriesApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
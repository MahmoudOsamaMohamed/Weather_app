package com.mahmoud.weatherapp

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore

class MyApp:Application() {
    // Create a DataStore by delegating to preferencesDataStore, specifying the name of your preferences file.
    val dataStore by preferencesDataStore(name = "settings")

    override fun onCreate() {
        super.onCreate()

        // Initialize your DataStore here if needed
    }
}
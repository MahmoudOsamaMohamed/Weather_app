package com.mahmoud.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.weatherapp.model.RemoteDataSource

class RemoteViewModelFactory(private val remoteDataSource: RemoteDataSource): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RemoteViewModel(remoteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
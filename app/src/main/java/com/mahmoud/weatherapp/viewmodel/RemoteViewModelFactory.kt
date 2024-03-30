package com.mahmoud.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.weatherapp.IReposatory
import com.mahmoud.weatherapp.Reposatory

class RemoteViewModelFactory(private val reposatory: IReposatory): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RemoteViewModel(reposatory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
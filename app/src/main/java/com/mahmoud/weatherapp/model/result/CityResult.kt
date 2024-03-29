package com.mahmoud.weatherapp.model.result

sealed class CityResult <out T> {
    data class Success<out T>(val data: T) : CityResult<T>()
    data class Error(val exception: Throwable) : CityResult<Nothing>()
    object Loading : CityResult<Nothing>()

}
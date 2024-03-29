package com.mahmoud.weatherapp.model.result

sealed class ForecastResult<out T> {
    data class Success<out T>(val data: T) : ForecastResult<T>()
    data class Error(val exception: Throwable) : ForecastResult<Nothing>()
    object Loading : ForecastResult<Nothing>()
}
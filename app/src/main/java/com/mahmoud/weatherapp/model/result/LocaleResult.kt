package com.mahmoud.weatherapp.model.result

sealed class LocaleResult<out T> {
    class Success<out T>(val data: T) : LocaleResult<T>()
    class Error(val exception: Throwable) : LocaleResult<Nothing>()
    object Loading : LocaleResult<Nothing>()
}
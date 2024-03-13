package com.mahmoud.weatherapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    val APP_KEY ="5517b550e852dc7a9a2e19a6c8b4adff"
    object RetrofitClient{
        val BASE_URL = "https://api.openweathermap.org/"
        val Second_URL="https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/geonames-all-cities-with-a-population-1000/"

        val apiForOpenWeather = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        val apiForOpenDataSoft = Retrofit.Builder()
            .baseUrl(Second_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    suspend fun getWeather(lat:String,lon:String) = RetrofitClient.apiForOpenWeather.getWeather(lat,lon,APP_KEY)

    suspend fun getLocales(q:String,limit:String) = RetrofitClient.apiForOpenWeather.getLocales(q,limit,APP_KEY)

    suspend fun getCities(where:String,limit:String) = RetrofitClient.apiForOpenDataSoft.getCities(where,limit)
}
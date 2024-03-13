package com.mahmoud.weatherapp.model

import com.example.example.CityResponse

import com.example.example.LocaleResponse
import com.example.example.Response
import retrofit2.http.GET
import retrofit2.http.Query

//const val APP_KEY ="5517b550e852dc7a9a2e19a6c8b4adff"
interface ApiService {
     @GET("data/2.5/weather")
     suspend fun getWeather(@Query("lat") lat:String,
                            @Query("lon") lon:String,
                            @Query("appid") appid:String ): Response
     @GET("records")
     suspend fun getCities(@Query("where") where:String, @Query("limit") limit:String): CityResponse
     @GET("geo/1.0/direct")
     suspend fun getLocales(@Query("q") q:String, @Query("limit") limit:String,@Query("appid") appid:String ): List<LocaleResponse>
}
package com.mahmoud.weatherapp.remote


import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import retrofit2.http.GET
import retrofit2.http.Query

//const val APP_KEY ="5517b550e852dc7a9a2e19a6c8b4adff"
interface ApiService {
     @GET("data/3.0/onecall")
     suspend fun getWeather(@Query("lat") lat:String,
                            @Query("lon") lon:String,
                            @Query("appid") appid:String,
                            @Query("units") units:String="metric",
                            @Query("lang") lang:String="en"): ForecastResponse
     @GET("/api/")
     suspend fun getCities(@Query("q") query:String, @Query("limit") limit:String): CityResponse
     @GET("geo/1.0/reverse")
     suspend fun getLocales(@Query("lat") lat:String,@Query("lon") lon:String, @Query("limit") limit:String,@Query("appid") appid:String ): List<LocaleResponse>
}
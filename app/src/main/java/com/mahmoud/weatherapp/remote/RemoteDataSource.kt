package com.mahmoud.weatherapp.remote


import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    val APP_KEY ="8dc12e644f5af0e96002bb8bb3fc6e19"
    object RetrofitClient{
        val OPEN_WEATHER_URL = "https://api.openweathermap.org/"
        val PHOTON_URL="https://photon.komoot.io"

        val apiForOpenWeather = Retrofit.Builder()
            .baseUrl(OPEN_WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        val apiForPhoton = Retrofit.Builder()
            .baseUrl(PHOTON_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
     fun getWeather(lat:String,lon:String,language: String): Flow<ForecastResult<ForecastResponse>> = flow {
         try {
             emit(ForecastResult.Loading)
             val response = RetrofitClient.apiForOpenWeather.getWeather(lat,lon,APP_KEY, lang = language.toLowerCase())
             emit(ForecastResult.Success(response))
         }catch (e:Exception){
             emit(ForecastResult.Error(e))
         }

     }

     fun getLocales(lat:String,lon:String,limit:String) : Flow<LocaleResult<List<LocaleResponse>>> = flow {
         try {
             emit(LocaleResult.Loading)
             val response = RetrofitClient.apiForOpenWeather.getLocales(lat,lon,limit,APP_KEY)
             emit(LocaleResult.Success(response))
         }catch (e:Exception){
             emit(LocaleResult.Error(e))
         }
     }

     fun getCities(q:String,limit:String) : Flow<CityResult<CityResponse>> = flow {
         try {
             emit(CityResult.Loading)
             val response = RetrofitClient.apiForPhoton.getCities(q,limit)
             emit(CityResult.Success(response))
         }catch (e:Exception){
             emit(CityResult.Error(e))
         }
     }
}
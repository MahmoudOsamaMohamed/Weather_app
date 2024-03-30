package com.mahmoud.weatherapp.fake

import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.mahmoud.weatherapp.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class FakeRemote(val forecastResponse: ForecastResponse,val cityResponse: CityResponse,val localeResponse: LocaleResponse):IRemoteDataSource {
    override fun getWeather(
        lat: String,
        lon: String,
        language: String
    ): Flow<ForecastResult<ForecastResponse>> {

        return if(forecastResponse != null)
            flowOf(ForecastResult.Success(forecastResponse))
        else
            flowOf(ForecastResult.Error(Throwable("Error")))
    }

    override fun getLocales(
        lat: String,
        lon: String,
        limit: String
    ): Flow<LocaleResult<List<LocaleResponse>>> {
        TODO("Not yet implemented")
    }

    override fun getCities(q: String, limit: String): Flow<CityResult<CityResponse>> {
        TODO("Not yet implemented")
    }
}
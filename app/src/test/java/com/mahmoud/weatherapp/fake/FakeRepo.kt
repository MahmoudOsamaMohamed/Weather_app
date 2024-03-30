package com.mahmoud.weatherapp.fake

import com.mahmoud.weatherapp.IReposatory
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.Current
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocalNames
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.SettingsPreferences
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeRepo(): IReposatory {
    override suspend fun updateSettingsPreferences(
        speedUnit: SpeedUnit?,
        language: Language?,
        tempUnit: TempUnit?,
        location: Location?,
        lat: String?,
        lon: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun readSettingsPreference(): SharedFlow<SettingsPreferences> {
        TODO("Not yet implemented")
    }

    override fun getWeather(
        lat: String,
        lon: String,
        language: String
    ): Flow<ForecastResult<ForecastResponse>> {
        return flowOf(ForecastResult.Success( ForecastResponse(30.0,30.0,"time zone",0,
             Current(0,0,0,0.9,0.0,0,0,0.0,
                0.0,0,0,0.0,0,0.0, listOf()
            ), listOf(), listOf(), listOf()
        ) ))
    }

    override fun getLocale(
        lat: String,
        lon: String,
        limit: String
    ): Flow<LocaleResult<List<LocaleResponse>>> {
       return flowOf(LocaleResult.Success(listOf(LocaleResponse("name",
           LocalNames(),0.0,0.0,"country"))))
    }

    override fun getCities(where: String, limit: String): Flow<CityResult<CityResponse>> {
        return flowOf(CityResult.Success(CityResponse(listOf(),"type")))
    }

    override fun getCashWeather(): Flow<List<CashWeather>> {
      return flowOf(listOf())
    }

    override fun getFavourate(): Flow<List<Favourate>> {
       return flowOf(listOf())
    }

    override suspend fun insertToCash(cashWeather: CashWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun insertToFavourate(favourate: Favourate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCash() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromFavourate(favourate: Favourate) {
        TODO("Not yet implemented")
    }

    override fun getAlarm(): Flow<List<AlarmItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertToAlarm(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromAlarm(id: Int) {
        TODO("Not yet implemented")
    }
}
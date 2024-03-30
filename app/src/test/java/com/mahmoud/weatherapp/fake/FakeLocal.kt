package com.mahmoud.weatherapp.fake

import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.SettingsPreferences
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.ILocalDataSource
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

class FakeLocal(val cashWeatherList:MutableList<CashWeather>,val favourateList:MutableList<Favourate>,val alarmList:MutableList<AlarmItem>):ILocalDataSource {
    override suspend fun insertToCash(cashWeather: CashWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCash() {
        cashWeatherList.clear()
    }

    override fun getAllCash(): Flow<List<CashWeather>> {
        val flow: Flow<List<CashWeather>> = flowOf(cashWeatherList)
        return flow

    }

    override suspend fun insertToFavourate(favourate: Favourate) {
        favourateList.add(favourate)
    }

    override suspend fun deleteFromFavourate(favourate: Favourate) {
        favourateList.remove(favourate)
    }

    override fun getAllFavourate(): Flow<List<Favourate>> {
        val flow: Flow<List<Favourate>> = flowOf(favourateList)
        return flow
    }

    override suspend fun insertToAlarm(alarmItem: AlarmItem) {
        alarmList.add(alarmItem)
    }

    override suspend fun deleteFromAlarm(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllAlarm(): Flow<List<AlarmItem>> {
        val flow: Flow<List<AlarmItem>> = flowOf(alarmList)
        return flow
    }

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
}
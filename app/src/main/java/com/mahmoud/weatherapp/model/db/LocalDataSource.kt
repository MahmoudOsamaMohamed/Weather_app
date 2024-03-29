package com.mahmoud.weatherapp.model.db

import android.content.Context
import com.mahmoud.weatherapp.alaram.AlarmItemDao
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.plcoding.alarmmanagerguide.AlarmItem

import kotlinx.coroutines.flow.Flow

class LocalDataSource(val context: Context) {
    private val cashWeatherDao: CashWeatherDao by lazy {
        val database = RoomDB.getDatabase(context)
        database.cashWeatherDao()
    }
    private val favourateDao: FavourateDao by lazy {
        val database = RoomDB.getDatabase(context)
        database.favourateDao()
    }
    private val alarmItemDao: AlarmItemDao by lazy {
        val database = RoomDB.getDatabase(context)
        database.alarmItemDao()
    }

    suspend fun insertToCash(cashWeather: CashWeather) = cashWeatherDao.insert(cashWeather)
    suspend fun deleteAllCash() = cashWeatherDao.delete()
    fun getAllCash(): Flow<List<CashWeather>> = cashWeatherDao.getAllResponses()

    suspend fun insertToFavourate(favourate: Favourate) = favourateDao.insert(favourate)
    suspend fun deleteFromFavourate(favourate: Favourate) = favourateDao.delete(favourate)
    fun getAllFavourate(): Flow<List<Favourate>> = favourateDao.getAllFavourate()
    suspend fun insertToAlarm(alarmItem: AlarmItem) = alarmItemDao.insert(alarmItem)
    suspend fun deleteFromAlarm(id: Int) = alarmItemDao.delete(id)
    fun getAllAlarm(): Flow<List<AlarmItem>> = alarmItemDao.getAllItems()
}
package com.mahmoud.weatherapp

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.SettingsPreferences
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.ILocalDataSource
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.mahmoud.weatherapp.remote.IRemoteDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


interface IReposatory {
    suspend fun updateSettingsPreferences(speedUnit: SpeedUnit? =null, language: Language? =null,
                                          tempUnit: TempUnit? =null, location: Location? =null,
                                          lat: String? =null, lon: String? =null)

    fun readSettingsPreference(): SharedFlow<SettingsPreferences>
    fun getWeather(lat: String, lon: String, language: String): Flow<ForecastResult<ForecastResponse>>
    fun getLocale(lat: String, lon: String, limit: String): Flow<LocaleResult<List<LocaleResponse>>>
    fun getCities(where: String, limit: String): Flow<CityResult<CityResponse>>
    fun getCashWeather(): Flow<List<CashWeather>>
    fun getFavourate(): Flow<List<Favourate>>

    suspend fun insertToCash(cashWeather: CashWeather)

    suspend fun insertToFavourate(favourate: Favourate)

    suspend fun deleteAllCash()

    suspend fun deleteFromFavourate(favourate: Favourate)
    fun getAlarm(): Flow<List<AlarmItem>>

    suspend fun insertToAlarm(alarmItem: AlarmItem)

    suspend fun deleteFromAlarm(id: Int)
}

class Reposatory(private val localDataSource: ILocalDataSource, private val remoteDataSource: IRemoteDataSource ) :
    IReposatory {


    override suspend fun updateSettingsPreferences(speedUnit: SpeedUnit?, language: Language?,
                                                   tempUnit: TempUnit?, location: Location?,
                                                   lat: String?, lon:String?
    ) {
localDataSource.updateSettingsPreferences(speedUnit,language,tempUnit,location,lat,lon)
    }
         override fun readSettingsPreference(): SharedFlow<SettingsPreferences> =localDataSource.readSettingsPreference()




    override fun getWeather(lat:String, lon:String, language:String) = remoteDataSource.getWeather(lat, lon,language= language)
    override fun getLocale(lat: String, lon: String, limit:String) = remoteDataSource.getLocales(lat,lon,limit)
    override fun getCities(where:String, limit:String) = remoteDataSource.getCities(where,limit)
    override fun getCashWeather() = localDataSource.getAllCash()
    override fun getFavourate() = localDataSource.getAllFavourate()
    override suspend fun insertToCash(cashWeather: CashWeather) = localDataSource.insertToCash(cashWeather)
    override suspend fun insertToFavourate(favourate: Favourate) = localDataSource.insertToFavourate(favourate)
    override suspend fun deleteAllCash() = localDataSource.deleteAllCash()
    override suspend fun deleteFromFavourate(favourate: Favourate) = localDataSource.deleteFromFavourate(favourate)
    override fun getAlarm() = localDataSource.getAllAlarm()
    override suspend fun insertToAlarm(alarmItem: AlarmItem) = localDataSource.insertToAlarm(alarmItem)
    override suspend fun deleteFromAlarm(id: Int) = localDataSource.deleteFromAlarm(id)

}
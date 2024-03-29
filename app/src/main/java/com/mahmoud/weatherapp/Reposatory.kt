package com.mahmoud.weatherapp

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.SettingsPreferences
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.LocalDataSource
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


val Context.dataStore by preferencesDataStore(name = "settings")

class Reposatory(private val localDataSource: LocalDataSource,private val remoteDataSource: RemoteDataSource) {


    suspend fun updateSettingsPreferences(speedUnit: SpeedUnit?=null,language: Language?=null,
                                          tempUnit: TempUnit?=null,location: Location?=null,
                                          lat: String?=null,lon:String?=null) {
        localDataSource.context.dataStore.edit {
Log.d("update datastore",it.toString())
            if(speedUnit!=null){ it[stringPreferencesKey("speedUnit")] = speedUnit.name
            }
            if(language!=null) {it[stringPreferencesKey("language")] = language.name
            }
            if(tempUnit!=null){ it[stringPreferencesKey("tempUnit")] = tempUnit.name
            }
            if(location!=null) {it[stringPreferencesKey("location")] = location.name
            }
            if(lat!=null && lon!=null) {it[stringPreferencesKey("lat")] = lat
                it[stringPreferencesKey("lon")] = lon
            }
        }}
         fun readSettingsPreference(): SharedFlow<SettingsPreferences> {
            return localDataSource.context.dataStore.data.map {
                val speedUnit= it[stringPreferencesKey("speedUnit")]?:SpeedUnit.KMPH.name
                val language= it[stringPreferencesKey("language")]?:Language.EN.name
                val tempUnit= it[stringPreferencesKey("tempUnit")]?:TempUnit.C.name
                val location= it[stringPreferencesKey("location")]?:Location.OFF.name
                val lat= it[stringPreferencesKey("lat")]?:""
                val lon= it[stringPreferencesKey("lon")]?:""
                SettingsPreferences(speedUnit,language,tempUnit,location,lat,lon)

            }.shareIn(scope = CoroutineScope(Dispatchers.IO),started = SharingStarted.Lazily,replay = 1)

        }




    fun getWeather(lat:String,lon:String,language:String) = remoteDataSource.getWeather(lat, lon,language= language)
    fun getLocale(lat: String,lon: String,limit:String) = remoteDataSource.getLocales(lat,lon,limit)
    fun getCities(where:String,limit:String) = remoteDataSource.getCities(where,limit)
    fun getCashWeather() = localDataSource.getAllCash()
    fun getFavourate() = localDataSource.getAllFavourate()
    suspend fun insertToCash(cashWeather: CashWeather) = localDataSource.insertToCash(cashWeather)
    suspend fun insertToFavourate(favourate: Favourate) = localDataSource.insertToFavourate(favourate)
    suspend fun deleteAllCash() = localDataSource.deleteAllCash()
    suspend fun deleteFromFavourate(favourate: Favourate) = localDataSource.deleteFromFavourate(favourate)
    fun getAlarm() = localDataSource.getAllAlarm()
    suspend fun insertToAlarm(alarmItem: AlarmItem) = localDataSource.insertToAlarm(alarmItem)
    suspend fun deleteFromAlarm(id: Int) = localDataSource.deleteFromAlarm(id)

}
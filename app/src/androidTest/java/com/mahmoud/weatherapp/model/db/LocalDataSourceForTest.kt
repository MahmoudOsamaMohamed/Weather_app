package com.mahmoud.weatherapp.model.db

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mahmoud.weatherapp.alaram.AlarmItemDao
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.SettingsPreferences
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

val Context.dataStore by preferencesDataStore(name = "settings")
class LocalDataSourceForTest(val favourateDao: FavourateDao,
                             val alarmItemDao: AlarmItemDao,
                             val cashWeatherDao: CashWeatherDao,
                             val context: Context):ILocalDataSource {

    override suspend fun updateSettingsPreferences(speedUnit: SpeedUnit?, language: Language?,
                                                   tempUnit: TempUnit?, location: Location?,
                                                   lat: String?, lon:String?
    ) {
        context.dataStore.edit {
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
    override fun readSettingsPreference(): SharedFlow<SettingsPreferences> {
        return  context.dataStore.data.map {
            val speedUnit= it[stringPreferencesKey("speedUnit")]?: SpeedUnit.KMPH.name
            val language= it[stringPreferencesKey("language")]?: Language.EN.name
            val tempUnit= it[stringPreferencesKey("tempUnit")]?: TempUnit.C.name
            val location= it[stringPreferencesKey("location")]?: Location.OFF.name
            val lat= it[stringPreferencesKey("lat")]?:""
            val lon= it[stringPreferencesKey("lon")]?:""
            SettingsPreferences(speedUnit,language,tempUnit,location,lat,lon)

        }.shareIn(scope = CoroutineScope(Dispatchers.IO),started = SharingStarted.Lazily,replay = 1)

    }
    override suspend fun insertToCash(cashWeather: CashWeather) = cashWeatherDao.insert(cashWeather)
    override suspend fun deleteAllCash() = cashWeatherDao.delete()
    override fun getAllCash(): Flow<List<CashWeather>> = cashWeatherDao.getAllResponses()

    override suspend fun insertToFavourate(favourate: Favourate) = favourateDao.insert(favourate)
    override suspend fun deleteFromFavourate(favourate: Favourate) = favourateDao.delete(favourate)
    override fun getAllFavourate(): Flow<List<Favourate>> = favourateDao.getAllFavourate()
    override suspend fun insertToAlarm(alarmItem: AlarmItem) = alarmItemDao.insert(alarmItem)
    override suspend fun deleteFromAlarm(id: Int) = alarmItemDao.delete(id)
    override fun getAllAlarm(): Flow<List<AlarmItem>> = alarmItemDao.getAllItems()
}
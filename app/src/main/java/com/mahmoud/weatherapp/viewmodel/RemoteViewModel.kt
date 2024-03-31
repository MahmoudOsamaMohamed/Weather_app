package com.mahmoud.weatherapp.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoud.weatherapp.IReposatory
import com.mahmoud.weatherapp.Reposatory
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

import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RemoteViewModel(private val repo: IReposatory):ViewModel(){
    private val _suggestions = MutableStateFlow<CityResult<CityResponse>>(CityResult.Loading)
    val suggestions: StateFlow<CityResult<CityResponse>> = _suggestions
    private val _settingsPreferences = MutableStateFlow<SettingsPreferences>(SettingsPreferences(SpeedUnit.KMPH.name,Language.EN.name,TempUnit.C.name,Location.OFF.name,"",""))
    val settingsPreferences:StateFlow<SettingsPreferences> = _settingsPreferences
    private val _alarms = MutableStateFlow<List<AlarmItem>>(emptyList())
    val alarms:StateFlow<List<AlarmItem>> = _alarms
    private val _onlineWeather = MutableStateFlow<ForecastResult<ForecastResponse>>(ForecastResult.Loading)
    val onlineWeather:StateFlow<ForecastResult<ForecastResponse>> = _onlineWeather
    private val _locales = MutableStateFlow<LocaleResult<List<LocaleResponse>>>(LocaleResult.Loading)
    val locales:StateFlow<LocaleResult<List<LocaleResponse>>> = _locales
    private val _cities = MutableStateFlow<CityResult<CityResponse>>(CityResult.Loading)
    //val cities:StateFlow<CityResult<CityResponse>> = _cities
    private val _favourate = MutableStateFlow<List<Favourate>>(emptyList())
    val favourate:StateFlow<List<Favourate>> = _favourate
    private val _cashedWeather = MutableStateFlow<List<CashWeather>>(emptyList())
    val cashedWeather:StateFlow<List<CashWeather>> = _cashedWeather
    fun updateSettingsPreferences(speedUnit: SpeedUnit?=null,language: Language?=null,tempUnit: TempUnit?=null,location: Location?=null,lat: String?=null,lon:String?=null) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateSettingsPreferences(speedUnit,language,tempUnit,location,lat,lon)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun searchCity(name:String, limit: String){
        viewModelScope.launch(Dispatchers.IO) {
         repo.getCities(name,limit).catch {
             _suggestions.value=CityResult.Error(it)
         }.collect{
             _suggestions.value=it
         }
    }}
    fun readSettingsPreference() {

        viewModelScope.launch(Dispatchers.IO) {
            repo.readSettingsPreference().collect {
                _settingsPreferences.value=it
            }
        }

    }

     fun getWeather(lat:String,lon:String,lang:String){
         viewModelScope.launch {
             repo.getWeather(lat,lon,lang).
                     catch {
                         _onlineWeather.value=ForecastResult.Error(it)
                     }.
                     collect {
                         _onlineWeather.value=it
                     }
         }

    }
    fun insertToCash(cashWeather: CashWeather){
        viewModelScope.launch {
            repo.insertToCash(cashWeather)
        }
    }
    fun deleteAllCash(){
        viewModelScope.launch {
            repo.deleteAllCash()
        }
    }
    fun insertToFavorites(favourate: Favourate){
        viewModelScope.launch {
            repo.insertToFavourate(favourate)
        }
    }
    fun deleteFromFavorites(favourate: Favourate){
        viewModelScope.launch {
            repo.deleteFromFavourate(favourate)
        }
    }
    fun getAllFavourates(){
        viewModelScope.launch {
            repo.getFavourate().
                    catch {
                        _favourate.value=emptyList()
                    }.
                    collect {
                        _favourate.value=it
                    }
        }
    }
    fun getAllCashed(){
        viewModelScope.launch {
            repo.getCashWeather().
                    catch {
                        _cashedWeather.value=emptyList()
                    }.
                    collect {
                        _cashedWeather.value=it
                    }
        }
    }
    fun get_Cities(q:String, limit:String){
        viewModelScope.launch {
            val cities = repo.getCities(q,limit)
            cities.catch {
                _cities.value=CityResult.Error(it)
            }
            cities.collect {
                _cities.value=it
            }
        }
    }

//    fun get_Locales(q:String, limit:String){
//        viewModelScope.launch {
//            val cities = remoteDataSource.getCities("alternate_names LIKE '*$q*'",limit)
//            cities.catch {
//                _cities.value=CityResult.Error(it)
//            }
//            cities.collect {
//                _cities.value=it
//            }
//            _cities.collect{cityResult->
//                when(cityResult){
//
//                    is CityResult.Success->{
//                        val list = cityResult.data.results
//                        Log.d("list of locales",list.toString())
//                        for( n in list){
//                            remoteDataSource.getLocales(n.coordinates?.lat.toString(),n.coordinates?.lon.toString(),"10").
//                            catch {
//                                _locales.value=LocaleResult.Error(it)
//                            }
//                                .collect {
//                                  //  Log.d("collect parametar",n.asciiName?:"")
//                                _locales.value=it
//                            }
//                        }
//
//                    }
//                    is CityResult.Error->{
//                        _locales.value=LocaleResult.Error(cityResult.exception)
//                        Log.d("error collect",cityResult.exception.toString())
//                    }
//
//                    else->{
//                        _locales.value=LocaleResult.Loading
//                        Log.d("loading collect",cityResult.toString())
//                    }
//                }
//
//            }
//
//        }
//    }
    fun get_Locales(lat: String,lon: String,limit:String) {
    viewModelScope.launch {
        repo.getLocale(lat, lon, limit).catch {
            _locales.value = LocaleResult.Error(it)
        }
            .collect {
                //  Log.d("collect parametar",n.asciiName?:"")
                _locales.value = it
            }
    }
}
    fun insertToAlarm(alarmItem: AlarmItem){
        viewModelScope.launch {
            repo.insertToAlarm(alarmItem)
        }
    }
    fun getAllAlarm(){
        viewModelScope.launch {
            repo.getAlarm().
                    collect {
                        _alarms.value=it
                    }
        }
    }
    fun deleteFromAlarm(id : Int){
        viewModelScope.launch {
            repo.deleteFromAlarm(id)
        }
    }

}

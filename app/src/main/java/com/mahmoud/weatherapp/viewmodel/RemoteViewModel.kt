package com.mahmoud.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.CityResponse
import com.example.example.LocaleResponse
import com.example.example.Response
import com.mahmoud.weatherapp.model.RemoteDataSource
import kotlinx.coroutines.launch

class RemoteViewModel(private val remoteDataSource: RemoteDataSource):ViewModel(){
    private val _response = MutableLiveData<Response>()
    val response:LiveData<Response> = _response
    private val _locales = MutableLiveData<LocaleResponse>()
    val locales:LiveData<LocaleResponse> = _locales
    private val _cities = MutableLiveData<CityResponse>()
    val cities:LiveData<CityResponse> = _cities
     fun getWeather(lat:String,lon:String){
         viewModelScope.launch {
        _response.postValue(remoteDataSource.getWeather(lat,lon))
         }

    }

    fun get_Locales(q:String, limit:String){
        viewModelScope.launch {
            val list:List<LocaleResponse> = remoteDataSource.getLocales(q,limit)
            _locales.postValue(list[0])
        }
    }
    fun get_Cities(where:String,limit:String){
        viewModelScope.launch {
            _cities.postValue(remoteDataSource.getCities(where,limit))
        }
    }

}
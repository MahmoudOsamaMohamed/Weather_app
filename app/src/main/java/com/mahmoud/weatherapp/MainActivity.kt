package com.mahmoud.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.weatherapp.model.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    var j=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory = RemoteViewModelFactory(RemoteDataSource())
        val viewModel = ViewModelProvider(this,factory).get(RemoteViewModel::class.java)
        //viewModel.getWeather("30.033333","31.233334")
        viewModel.response.observe(this){
            Log.d("observe response",it.weather[0].toString())
        }
        //viewModel.get_Locales("Cairo","5")
        viewModel.locales.observe(this){
            Log.d("observe locales",j.toString()+" "+it.localNames?.ar.toString())
        }
        viewModel.get_Cities("alternate_names LIKE '*ب*'","30")
        viewModel.cities.observe(this){

Thread{
            for(i in it.results) {
                Log.d("observe cities ", j.toString()+" "+ i.asciiName?:"")
                viewModel.get_Locales( i.asciiName?:"", "1")
                sleep(500)
                j++
            }      }.start() }
    }
}
package com.mahmoud.weatherapp.model.db

import androidx.room.Entity

@Entity(tableName = "favourate", primaryKeys = [ "lat", "lon"])
data class Favourate(
    val cityName: String,
    val lat:String,
    val lon:String,
    val cityNameAr: String
)
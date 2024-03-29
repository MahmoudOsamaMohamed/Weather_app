package com.mahmoud.weatherapp.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import java.io.Serializable
@Entity(tableName = "cashWeather")
data class CashWeather (
    val forecastResponse: ForecastResponse,
    @PrimaryKey val cityName: String,
    val cityNameAr: String
): Serializable
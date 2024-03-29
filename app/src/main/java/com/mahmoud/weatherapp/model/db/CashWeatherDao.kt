package com.mahmoud.weatherapp.model.db

import androidx.room.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


import kotlinx.coroutines.flow.Flow

@Dao
interface CashWeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cashWeather: CashWeather)
    @Query("DeLETE FROM cashWeather")
    suspend fun delete()
    @Query("SELECT * FROM cashWeather")
    fun getAllResponses(): Flow<List<CashWeather>>
}
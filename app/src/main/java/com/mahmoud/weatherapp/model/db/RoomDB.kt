package com.mahmoud.weatherapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.room.TypeConverters
import com.mahmoud.weatherapp.alaram.AlarmItemDao
import com.mahmoud.weatherapp.model.Pojos.RoomConverter
import com.plcoding.alarmmanagerguide.AlarmItem

@Database(entities = [CashWeather::class, Favourate::class, AlarmItem::class], version = 6)
@TypeConverters(RoomConverter::class)
abstract class RoomDB:RoomDatabase() {

    abstract fun cashWeatherDao(): CashWeatherDao
    abstract fun favourateDao(): FavourateDao

    abstract fun alarmItemDao(): AlarmItemDao
    companion object{
        @Volatile
        private var INSTANCE: RoomDB? = null
        fun getDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "WeatherDB"
                ).build().also { INSTANCE = it }
            }

        }
    }
}
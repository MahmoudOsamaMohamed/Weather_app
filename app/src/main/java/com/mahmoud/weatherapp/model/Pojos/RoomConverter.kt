package com.mahmoud.weatherapp.model.Pojos
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RoomConverter {
@TypeConverter
fun toForecastResponse(string: String): ForecastResponse {
    val type = object : TypeToken<ForecastResponse>() {}.type
    return Gson().fromJson(string, type)
}

    @TypeConverter
    fun fromForecastResponse(forecastResponse: ForecastResponse): String {
        return Gson().toJson(forecastResponse)
    }
    @TypeConverter
    fun fromCurrent(current: Current): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun toCurrent(currentString: String): Current {
        val type = object : TypeToken<Current>() {}.type
        return Gson().fromJson(currentString, type)
    }

    @TypeConverter
    fun fromMinutely(minutely: List<Minutely>): String {
        return Gson().toJson(minutely)
    }

    @TypeConverter
    fun toMinutely(minutelyString: String): List<Minutely> {
        val type = object : TypeToken<List<Minutely>>() {}.type
        return Gson().fromJson(minutelyString, type)
    }

    @TypeConverter
    fun fromHourly(hourly: List<Hourly>): String {
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun toHourly(hourlyString: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(hourlyString, type)
    }

    @TypeConverter
    fun fromDaily(daily: List<Daily>): String {
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun toDaily(dailyString: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(dailyString, type)
    }
}

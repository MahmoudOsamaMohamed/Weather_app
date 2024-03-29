
package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Daily (

	@SerializedName("dt") val dt : Int,
	@SerializedName("sunrise") val sunrise : Int,
	@SerializedName("sunset") val sunset : Int,
	@SerializedName("moonrise") val moonrise : Int,
	@SerializedName("moonset") val moonset : Int,
	@SerializedName("moon_phase") val moon_phase : Double,
	@SerializedName("summary") val summary : String,
	@SerializedName("temp") val temp : Temp,
	@SerializedName("feels_like") val feels_like : Feels_like,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int,
	@SerializedName("dew_point") val dew_point : Double,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_deg") val wind_deg : Int,
	@SerializedName("wind_gust") val wind_gust : Double,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("clouds") val clouds : Int,
	@SerializedName("pop") val pop : Double,
	@SerializedName("rain") val rain : Double,
	@SerializedName("uvi") val uvi : Double
):Serializable
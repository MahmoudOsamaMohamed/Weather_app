package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName

data class Properties (

	@SerializedName("osm_type") val osm_type : String,
	@SerializedName("osm_id") val osm_id : Long,
	@SerializedName("extent") val extent : List<Double>,
	@SerializedName("country") val country : String,
	@SerializedName("osm_key") val osm_osm_key : String,
	@SerializedName("countrycode") val countrycode : String,
	@SerializedName("osm_value") val osm_value : String,
	@SerializedName("name") val name : String,
	@SerializedName("type") val type : String
)
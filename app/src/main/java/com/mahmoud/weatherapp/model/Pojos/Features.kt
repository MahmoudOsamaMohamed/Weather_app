package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName

data class Features (

	@SerializedName("geometry") val geometry : Geometry,
	@SerializedName("type") val type : String,
	@SerializedName("properties") val properties : Properties
)
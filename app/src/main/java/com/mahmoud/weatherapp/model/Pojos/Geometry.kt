package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName
data class Geometry (

	@SerializedName("coordinates") val coordinates : List<Double>,
	@SerializedName("type") val type : String
)
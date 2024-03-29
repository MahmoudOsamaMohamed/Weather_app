
package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Feels_like (

	@SerializedName("day") val day : Double,
	@SerializedName("night") val night : Double,
	@SerializedName("eve") val eve : Double,
	@SerializedName("morn") val morn : Double
): Serializable
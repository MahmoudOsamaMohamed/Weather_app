
package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName
data class Minutely (

	@SerializedName("dt") val dt : Int,
	@SerializedName("precipitation") val precipitation : Double
)
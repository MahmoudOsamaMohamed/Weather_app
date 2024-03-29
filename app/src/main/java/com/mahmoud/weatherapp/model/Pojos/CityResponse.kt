package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName


data class CityResponse (


  @SerializedName("features") val features : List<Features>,
  @SerializedName("type") val type : String

)
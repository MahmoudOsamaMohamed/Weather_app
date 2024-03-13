package com.example.example

import com.google.gson.annotations.SerializedName


data class Coordinates (

  @SerializedName("lon" ) var lon : Double? = null,
  @SerializedName("lat" ) var lat : Double? = null

)
package com.mahmoud.weatherapp.model.Pojos

import com.google.gson.annotations.SerializedName


data class LocaleResponse (

  @SerializedName("name"        ) var name       : String?     = null,
  @SerializedName("local_names" ) var localNames : LocalNames? = LocalNames(),
  @SerializedName("lat"         ) var lat        : Double?     = null,
  @SerializedName("lon"         ) var lon        : Double?     = null,
  @SerializedName("country"     ) var country    : String?     = null,
 // @SerializedName("state"       ) var state      : String?     = null

)
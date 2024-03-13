package com.example.example

import com.google.gson.annotations.SerializedName


data class CityResponse (

  @SerializedName("total_count" ) var totalCount : Int?               = null,
  @SerializedName("results"     ) var results    : ArrayList<Results> = arrayListOf()

)
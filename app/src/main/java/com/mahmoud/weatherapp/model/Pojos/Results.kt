package com.example.example

import com.google.gson.annotations.SerializedName


data class Results (

  @SerializedName("geoname_id"        ) var geonameId        : String?           = null,
  @SerializedName("name"              ) var name             : String?           = null,
  @SerializedName("ascii_name"        ) var asciiName        : String?           = null,
  @SerializedName("alternate_names"   ) var alternateNames   : ArrayList<String> = arrayListOf(),
  @SerializedName("feature_class"     ) var featureClass     : String?           = null,
  @SerializedName("feature_code"      ) var featureCode      : String?           = null,
  @SerializedName("country_code"      ) var countryCode      : String?           = null,
  @SerializedName("cou_name_en"       ) var couNameEn        : String?           = null,
  @SerializedName("country_code_2"    ) var countryCode2     : String?           = null,
  @SerializedName("admin1_code"       ) var admin1Code       : String?           = null,
  @SerializedName("admin2_code"       ) var admin2Code       : String?           = null,
  @SerializedName("admin3_code"       ) var admin3Code       : String?           = null,
  @SerializedName("admin4_code"       ) var admin4Code       : String?           = null,
  @SerializedName("population"        ) var population       : Int?              = null,
  @SerializedName("elevation"         ) var elevation        : String?           = null,
  @SerializedName("dem"               ) var dem              : Int?              = null,
  @SerializedName("timezone"          ) var timezone         : String?           = null,
  @SerializedName("modification_date" ) var modificationDate : String?           = null,
  @SerializedName("label_en"          ) var labelEn          : String?           = null,
  @SerializedName("coordinates"       ) var coordinates      : Coordinates?      = Coordinates()

)
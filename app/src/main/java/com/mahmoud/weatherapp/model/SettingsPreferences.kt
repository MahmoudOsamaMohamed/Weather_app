package com.mahmoud.weatherapp.model

data class SettingsPreferences(
    val speedUnit: String,
    val language: String,
    val tempUnit: String,
    val location: String,
    val lat : String,
    val lon : String
)
enum class SpeedUnit{
    KMPH,
    MPH
}
enum class TempUnit{
    C,
    F,
    K
}
enum class Language{
    EN,
    AR
}
enum class Location{
    ON,
    OFF
}
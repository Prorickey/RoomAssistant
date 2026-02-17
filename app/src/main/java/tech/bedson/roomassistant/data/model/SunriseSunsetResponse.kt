package tech.bedson.roomassistant.data.model

data class SunriseSunsetResponse(
    val results: SunriseSunsetResults,
    val status: String
)

data class SunriseSunsetResults(
    val sunrise: String,
    val sunset: String,
    val solar_noon: String,
    val day_length: Long,
    val civil_twilight_begin: String,
    val civil_twilight_end: String
)

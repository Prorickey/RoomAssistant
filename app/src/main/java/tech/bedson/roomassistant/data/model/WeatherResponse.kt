package tech.bedson.roomassistant.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val weather: List<WeatherCondition>,
    val main: MainWeather,
    val name: String
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainWeather(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val humidity: Int
)

package com.chelsea.panpanweatherapp.ui.model

data class WeatherResponse(
    val weather: List<WeatherDescription>,
    val main: MainStats,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val name: String,
    val dt: Long,
    val cod: Int
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
) {
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$icon@4x.png"
}

data class MainStats(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)

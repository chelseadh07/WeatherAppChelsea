package com.chelsea.panpanweatherapp.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val weather: List<WeatherDto>,
    val main: MainDto,
    val wind: WindDto,
    val sys: SystemInfoDto,
    val name: String,
    val rain: RainDto? = null,
    val clouds: CloudsDto,
    val dt: Long,
    val cod: Int
)

data class WeatherDto(
    val main: String,
    val description: String,
    val icon: String
)

data class MainDto(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int
)

data class WindDto(
    val speed: Double
)

data class SystemInfoDto(
    val sunrise: Long,
    val sunset: Long
)

data class RainDto(
    @SerializedName("1h")
    val oneHour: Double? = null
)

data class CloudsDto(
    val all: Int
)

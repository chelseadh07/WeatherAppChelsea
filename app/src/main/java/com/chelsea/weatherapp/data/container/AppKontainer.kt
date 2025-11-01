package com.chelsea.panpanweatherapp.data.container

import com.chelsea.panpanweatherapp.data.repository.RepoJaringan
import com.chelsea.panpanweatherapp.data.repository.WeatherRepository
import com.chelsea.panpanweatherapp.data.service.WeatherService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppKontainer {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "5ddf6b82b1ca40e8d7d1cf34c5d5e9b7"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }

    // ekspos sebagai WeatherRepository (interface tetap sama)
    val repo: WeatherRepository by lazy {
        RepoJaringan(weatherService)
    }
}

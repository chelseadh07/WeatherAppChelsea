package com.chelsea.panpanweatherapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.panpanweatherapp.data.repository.WeatherRepository
import com.chelsea.panpanweatherapp.ui.model.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WeatherUiState {
    data class Success(val weather: WeatherResponse) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
    object Loading : WeatherUiState
    object Initial : WeatherUiState
}

class WeatherViewModel(
    private val repo: WeatherRepository
) : ViewModel() {

    var uiState: WeatherUiState by mutableStateOf(WeatherUiState.Initial)
        private set

    // super singkat style C, default Surabaya
    var kota by mutableStateOf("Surabaya")

    fun getCuaca() {
        viewModelScope.launch {
            uiState = WeatherUiState.Loading

            uiState = try {
                val res = repo.getWeather(kota)
                if (res.cod.toString().startsWith("4")) {
                    WeatherUiState.Error("City not found (HTTP ${res.cod})")
                } else {
                    WeatherUiState.Success(res)
                }
            } catch (e: IOException) {
                WeatherUiState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                WeatherUiState.Error("HTTP ${e.code()}: ${errorBody ?: "Unknown HTTP error"}")
            } catch (e: Exception) {
                WeatherUiState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }
}

package tech.bedson.roomassistant.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tech.bedson.roomassistant.BuildConfig
import tech.bedson.roomassistant.data.api.RetrofitClient

data class WeatherUiState(
    val temperature: Double = 0.0,
    val feelsLike: Double = 0.0,
    val description: String = "",
    val iconCode: String = "",
    val weatherConditionId: Int = 800,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    companion object {
        private const val NCSSM_LAT = 35.9847
        private const val NCSSM_LON = -78.8986
        private const val REFRESH_INTERVAL_MS = 15 * 60 * 1000L // 15 minutes
    }

    init {
        fetchWeather()
        startAutoRefresh()
    }

    private fun fetchWeather() {
        if (apiKey.isBlank()) return
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = RetrofitClient.weatherApi.getCurrentWeather(
                    lat = NCSSM_LAT,
                    lon = NCSSM_LON,
                    apiKey = apiKey
                )
                val condition = response.weather.firstOrNull()
                _uiState.value = WeatherUiState(
                    temperature = response.main.temp,
                    feelsLike = response.main.feelsLike,
                    description = condition?.description ?: "",
                    iconCode = condition?.icon ?: "",
                    weatherConditionId = condition?.id ?: 800,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Failed to fetch weather", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(REFRESH_INTERVAL_MS)
                fetchWeather()
            }
        }
    }
}

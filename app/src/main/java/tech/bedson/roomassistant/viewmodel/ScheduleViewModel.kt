package tech.bedson.roomassistant.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tech.bedson.roomassistant.data.ScheduleRepository
import tech.bedson.roomassistant.data.api.RetrofitClient
import tech.bedson.roomassistant.data.model.MealTimes
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class ScheduleUiState(
    val currentEvent: String = "",
    val minutesRemaining: Int = 0,
    val secondsRemaining: Int = 0,
    val nextEvent: String = "",
    val mealTimes: MealTimes = MealTimes(null, null, null),
    val sunrise: String = "",
    val sunset: String = ""
)

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScheduleRepository(application)

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState

    private var totalSecondsRemaining = 0

    init {
        updateScheduleState()
        startCountdown()
        fetchSunriseSunset()
    }

    private fun updateScheduleState() {
        val currentEvent = repository.getCurrentEvent()
        val nextEvent = repository.getNextEvent()
        val minutesUntilNext = repository.getMinutesUntilNextEvent()
        val mealTimes = repository.getMealTimes()

        totalSecondsRemaining = minutesUntilNext * 60

        _uiState.value = _uiState.value.copy(
            currentEvent = currentEvent?.event ?: "No Event",
            minutesRemaining = minutesUntilNext,
            secondsRemaining = 0,
            nextEvent = nextEvent?.event ?: "Done for today",
            mealTimes = mealTimes
        )
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (totalSecondsRemaining > 0) {
                    totalSecondsRemaining--
                    _uiState.value = _uiState.value.copy(
                        minutesRemaining = totalSecondsRemaining / 60,
                        secondsRemaining = totalSecondsRemaining % 60
                    )
                } else {
                    updateScheduleState()
                }
            }
        }
    }

    private fun fetchSunriseSunset() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.sunriseSunsetApi.getSunriseSunset(
                    lat = 35.9847,
                    lng = -78.8986
                )
                val sunriseFormatted = formatUtcTime(response.results.sunrise)
                val sunsetFormatted = formatUtcTime(response.results.sunset)
                _uiState.value = _uiState.value.copy(
                    sunrise = sunriseFormatted,
                    sunset = sunsetFormatted
                )
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Failed to fetch sunrise/sunset", e)
            }
        }
    }

    private fun formatUtcTime(isoTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
            val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
            outputFormat.timeZone = TimeZone.getDefault()
            val date = inputFormat.parse(isoTime)
            if (date != null) outputFormat.format(date) else isoTime
        } catch (e: Exception) {
            // Fallback: try without timezone offset parsing for older APIs
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'", Locale.US)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
                outputFormat.timeZone = TimeZone.getDefault()
                val date = inputFormat.parse(isoTime)
                if (date != null) outputFormat.format(date) else isoTime
            } catch (e2: Exception) {
                isoTime
            }
        }
    }
}

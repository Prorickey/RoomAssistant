package tech.bedson.roomassistant.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tech.bedson.roomassistant.BuildConfig
import tech.bedson.roomassistant.data.api.RetrofitClient
import tech.bedson.roomassistant.data.model.GoveeCmd
import tech.bedson.roomassistant.data.model.GoveeCommandRequest

data class LedPreset(
    val name: String,
    val color: Color,
    val colorHex: Int
)

data class GoveeUiState(
    val deviceId: String = "",
    val model: String = "",
    val isPoweredOn: Boolean = false,
    val activePresetIndex: Int = -1,
    val isLoading: Boolean = false,
    val error: String? = null
)

class GoveeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GoveeUiState())
    val uiState: StateFlow<GoveeUiState> = _uiState

    val presets = listOf(
        LedPreset("Lofi Purple", Color(0xFFB24BF3), 0xB24BF3),
        LedPreset("Sunset", Color(0xFFFF6B35), 0xFF6B35),
        LedPreset("Ocean", Color(0xFF006994), 0x006994),
        LedPreset("Forest", Color(0xFF228B22), 0x228B22),
        LedPreset("Warm Reading", Color(0xFFFFB347), 0xFFB347),
        LedPreset("Party", Color(0xFFFF2D95), 0xFF2D95)
    )

    private val apiKey = BuildConfig.GOVEE_API_KEY

    init {
        fetchDevices()
    }

    private fun fetchDevices() {
        if (apiKey.isBlank()) return
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = RetrofitClient.goveeApi.getDevices(apiKey)
                val device = response.data?.devices?.firstOrNull()
                if (device != null) {
                    _uiState.value = _uiState.value.copy(
                        deviceId = device.device,
                        model = device.model,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No devices found"
                    )
                }
            } catch (e: Exception) {
                Log.e("GoveeViewModel", "Failed to fetch devices", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun togglePower() {
        val state = _uiState.value
        if (state.deviceId.isBlank() || apiKey.isBlank()) return

        viewModelScope.launch {
            try {
                val newPowerState = !state.isPoweredOn
                val cmd = GoveeCommandRequest(
                    device = state.deviceId,
                    model = state.model,
                    cmd = GoveeCmd("turn", if (newPowerState) "on" else "off")
                )
                RetrofitClient.goveeApi.sendCommand(apiKey, cmd)
                _uiState.value = state.copy(isPoweredOn = newPowerState)
            } catch (e: Exception) {
                Log.e("GoveeViewModel", "Failed to toggle power", e)
            }
        }
    }

    fun applyPreset(index: Int) {
        val state = _uiState.value
        if (state.deviceId.isBlank() || apiKey.isBlank()) return

        viewModelScope.launch {
            try {
                val preset = presets[index]
                val r = (preset.colorHex shr 16) and 0xFF
                val g = (preset.colorHex shr 8) and 0xFF
                val b = preset.colorHex and 0xFF

                // Ensure light is on first
                if (!state.isPoweredOn) {
                    val turnOnCmd = GoveeCommandRequest(
                        device = state.deviceId,
                        model = state.model,
                        cmd = GoveeCmd("turn", "on")
                    )
                    RetrofitClient.goveeApi.sendCommand(apiKey, turnOnCmd)
                }

                val colorCmd = GoveeCommandRequest(
                    device = state.deviceId,
                    model = state.model,
                    cmd = GoveeCmd("color", mapOf("r" to r, "g" to g, "b" to b))
                )
                RetrofitClient.goveeApi.sendCommand(apiKey, colorCmd)
                _uiState.value = state.copy(
                    isPoweredOn = true,
                    activePresetIndex = index
                )
            } catch (e: Exception) {
                Log.e("GoveeViewModel", "Failed to apply preset", e)
            }
        }
    }
}

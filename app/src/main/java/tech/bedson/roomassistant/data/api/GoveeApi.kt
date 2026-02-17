package tech.bedson.roomassistant.data.api

import tech.bedson.roomassistant.data.model.GoveeCommandRequest
import tech.bedson.roomassistant.data.model.GoveeDeviceListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface GoveeApi {

    @GET("v1/devices")
    suspend fun getDevices(
        @Header("Govee-API-Key") apiKey: String
    ): GoveeDeviceListResponse

    @PUT("v1/devices/control")
    suspend fun sendCommand(
        @Header("Govee-API-Key") apiKey: String,
        @Body command: GoveeCommandRequest
    ): Any
}

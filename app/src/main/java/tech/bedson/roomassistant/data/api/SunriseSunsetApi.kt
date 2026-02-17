package tech.bedson.roomassistant.data.api

import tech.bedson.roomassistant.data.model.SunriseSunsetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SunriseSunsetApi {

    @GET("json")
    suspend fun getSunriseSunset(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("formatted") formatted: Int = 0
    ): SunriseSunsetResponse
}

package com.fabianofranca.weathercock.infrastructure.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun weather(
        @Query("q") locale: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String
    ): Call<WeatherRaw>

    @GET("forecast")
    fun forecast(
        @Query("q") locale: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String
    ): Call<ForecastRaw>
}
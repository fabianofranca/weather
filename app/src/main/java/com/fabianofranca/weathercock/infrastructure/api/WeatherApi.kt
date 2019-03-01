package com.fabianofranca.weathercock.infrastructure.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    @Headers("Cache-Control: no-cache")
    fun weather(
        @Query("q") locale: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String
    ): Call<WeatherRaw>

    @GET("forecast")
    @Headers("Cache-Control: no-cache")
    fun forecast(
        @Query("q") locale: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String
    ): Call<ForecastRaw>


    @GET("uvi")
    @Headers("Cache-Control: no-cache")
    fun uvi(
        @Query("APPID") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<UviRaw>
}
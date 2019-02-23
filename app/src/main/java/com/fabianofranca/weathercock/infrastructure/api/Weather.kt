package com.fabianofranca.weathercock.infrastructure.api

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class WeatherRaw(val weather: List<WeatherItemRaw>, val main: MainRaw, val dt: Date)

@JsonClass(generateAdapter = true)
data class WeatherItemRaw(val id: Int)

@JsonClass(generateAdapter = true)
data class MainRaw(val temp: Float)
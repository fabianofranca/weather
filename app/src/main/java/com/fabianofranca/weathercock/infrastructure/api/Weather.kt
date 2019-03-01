package com.fabianofranca.weathercock.infrastructure.api

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class WeatherRaw(val coord: CoordRaw?, val weather: List<WeatherItemRaw>, val main: MainRaw, val dt: Date)

@JsonClass(generateAdapter = true)
data class WeatherItemRaw(val id: Int)

@JsonClass(generateAdapter = true)
data class MainRaw(val temp: Float)

@JsonClass(generateAdapter = true)
data class CoordRaw(val lon: Double, val lat: Double)
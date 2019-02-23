package com.fabianofranca.weathercock.infrastructure.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastRaw(val list: List<WeatherRaw>)
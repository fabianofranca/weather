package com.fabianofranca.weathercock.entities

import java.util.*

data class Weather(
    val condition: WeatherCondition,
    val temperature: Int,
    val date: Date,
    var fiveDays: List<Weather>? = null
)
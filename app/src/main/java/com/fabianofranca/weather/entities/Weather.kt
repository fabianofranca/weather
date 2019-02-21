package com.fabianofranca.weather.entities

import java.util.*

data class Weather(val condition: WeatherCondition, val temperature: Float, val date: Date)
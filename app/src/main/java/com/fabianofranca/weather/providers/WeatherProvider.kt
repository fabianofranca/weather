package com.fabianofranca.weather.providers

import com.fabianofranca.weather.entities.Locale
import com.fabianofranca.weather.entities.Weather

interface WeatherProvider {
    fun current(locale: Locale) : Weather
    fun fiveDay(locale: Locale) : List<Weather>
}
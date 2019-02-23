package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.entities.Locale
import com.fabianofranca.weathercock.entities.Weather

interface WeatherProvider {
    fun current(locale: Locale) : Weather
    fun fiveDay(locale: Locale) : List<Weather>
}
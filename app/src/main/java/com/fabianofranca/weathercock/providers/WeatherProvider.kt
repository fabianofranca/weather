package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.entities.Weather

interface WeatherProvider {
    fun current(locale: Location, units: Units): Weather
    fun fiveDay(locale: Location, units: Units): List<Weather>
}
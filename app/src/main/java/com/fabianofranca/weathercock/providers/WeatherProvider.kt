package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.api.UviRaw
import com.fabianofranca.weathercock.entities.Weather

interface WeatherProvider {
    fun current(location: Location, units: Units): Weather
    fun fiveDay(location: Location, units: Units): List<Weather>
    fun uvi(latitude: Double, longitude: Double): UviRaw
}
package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather

interface WeatherCacheRepository {

    fun pull(location: Location) : Weather

    fun push(location: Location, current: Weather, fiveDays: List<Weather>)
}
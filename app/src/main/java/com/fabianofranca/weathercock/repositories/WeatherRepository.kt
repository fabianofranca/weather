package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather

interface WeatherRepository {
    val failure: LiveData<Exception>
    fun weather(location: Location? = null): LiveData<Weather>
    fun location(): LiveData<Location>
}
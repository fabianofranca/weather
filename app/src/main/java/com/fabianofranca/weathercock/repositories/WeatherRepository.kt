package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather
import java.util.*

interface WeatherRepository {
    val failure: LiveData<Exception>
    val location: LiveData<Location>
    val updated: LiveData<Date>

    fun weather(location: Location? = null): LiveData<Weather>
}
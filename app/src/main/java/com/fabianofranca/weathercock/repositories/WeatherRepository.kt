package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import com.fabianofranca.weathercock.entities.Weather

interface WeatherRepository {
    fun weather(): LiveData<Weather>
}
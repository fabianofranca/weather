package com.fabianofranca.weather.repositories

import android.arch.lifecycle.LiveData
import com.fabianofranca.weather.entities.Locale
import com.fabianofranca.weather.entities.Weather

interface WeatherRepository {
    fun current(locale: Locale) : LiveData<Weather>
    fun fiveDay(locale: Locale) : LiveData<List<Weather>>
}
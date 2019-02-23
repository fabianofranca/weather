package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import com.fabianofranca.weathercock.entities.Locale
import com.fabianofranca.weathercock.entities.Weather

interface WeatherRepository {
    fun current(locale: Locale) : LiveData<Weather>
    fun fiveDay(locale: Locale) : LiveData<List<Weather>>
}
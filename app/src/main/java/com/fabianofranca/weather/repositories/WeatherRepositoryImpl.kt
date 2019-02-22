package com.fabianofranca.weather.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.fabianofranca.weather.entities.Locale
import com.fabianofranca.weather.entities.Weather
import com.fabianofranca.weather.providers.WeatherApiProvider
import com.fabianofranca.weather.providers.WeatherProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherRepositoryImpl(private val provider: WeatherProvider = WeatherApiProvider()) :
    WeatherRepository {

    private val current = MutableLiveData<Weather>()

    private val fiveDay = MutableLiveData<List<Weather>>()

    override fun current(locale: Locale): LiveData<Weather> {

        GlobalScope.launch {
            current.value = provider.current(locale)
        }

        return current
    }

    override fun fiveDay(locale: Locale): LiveData<List<Weather>> {

        GlobalScope.launch {
            fiveDay.value = provider.fiveDay(locale)
        }

        return fiveDay
    }
}